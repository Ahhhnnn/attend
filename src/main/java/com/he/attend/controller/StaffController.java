package com.he.attend.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.JsonResult;
import com.he.attend.common.PageResult;
import com.he.attend.model.*;
import com.he.attend.service.RuleService;
import com.he.attend.service.StaffService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("${api.version}/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private RuleService ruleService;
    @RequestMapping("/insert")
    public PageResult insertStaff(Staff staff){

        if(staffService.insert(staff)){
            return new PageResult("添加成功",200);
        }
        return new PageResult("添加失败",400);
    }

    @RequestMapping("/update")
    public PageResult updateStaff(Staff staff){

        if(staffService.updateById(staff)){
            return new PageResult("修改成功",200);
        }
        return new PageResult("修改失败",400);
    }

    @RequestMapping("/delete/{staffId}")
    public PageResult delteStaff(@PathVariable("staffId") Integer staffId){

        if(staffService.deleteById(staffId)){
            return new PageResult("删除成功",200);
        }
        return new PageResult("删除失败",400);
    }
    @ApiOperation(value = "查询所有员工", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @RequestMapping("/query")
    public PageResult<Staff> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }
        Page<Staff> userPage = new Page<>(page, limit);
        EntityWrapper<Staff> wrapper = new EntityWrapper<>();
        if (searchKey != null && !searchKey.trim().isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
            if(searchKey.equals("staff_name")||searchKey.equals("phone")){
                wrapper.like(searchKey,searchValue);
            }else {
                wrapper.eq(searchKey, searchValue);
            }
        }
        wrapper.orderBy("create_time", true);
        staffService.selectPage(userPage, wrapper);
        List<Staff> userList = userPage.getRecords();
        return new PageResult<>(userList, userPage.getTotal());
    }

    @ApiOperation(value = "修改员工状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "staffId", value = "员工Id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "state", value = "状态：0正常，1冻结", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @PutMapping("/state")
    public JsonResult updateState(Integer staffId, Integer state) {
        if (state == null || (state != 0 && state != 1)) {
            return JsonResult.error("state值需要在[0,1]中");
        }
        Staff staff=new Staff();
        staff.setState(state);
        staff.setStaffId(staffId);
        if (staffService.updateById(staff)) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    /**
     * 查询所有状态为0 的员工
     * @return
     */
    @RequestMapping("/queryAll")
    public PageResult<Staff> queryAll(){
        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("state",0);
        List<Staff> staffList =staffService.selectList(entityWrapper);
        return new PageResult<>(200,"查询员工成功",staffList.size(),staffList);
    }

    /**
     *
     * @return
     */
    @RequestMapping("/queryAllNotRepeat")
    public PageResult<Staff> queryAllNotRepeat(){
        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("state",0);
        List<Staff> staffList =staffService.selectList(entityWrapper);
        List<Rule> ruleList=ruleService.query();
        List<String> staffidString=new ArrayList<>();
        for(Rule rule:ruleList){
            staffidString.add(rule.getStaffIds());
        }
        List<Integer> staffIds=new ArrayList<>();
        for(String staffid:staffidString){
            for(String staff:staffid.split(",")){
                staffIds.add(Integer.valueOf(staff));
            }
        }
        List<Staff> finalStaff=new ArrayList<>();
        finalStaff.addAll(staffList);
        //去重
        for(Staff staff:staffList){
            if(staffIds.contains(staff.getStaffId())){
                finalStaff.remove(staff);
            }
        }
        return new PageResult<>(200,"查询员工成功",finalStaff.size(),finalStaff);
    }
}
