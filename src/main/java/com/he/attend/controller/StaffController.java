package com.he.attend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.JsonResult;
import com.he.attend.common.PageResult;
import com.he.attend.common.utils.PowerUtil;
import com.he.attend.model.*;
import com.he.attend.service.AttendService;
import com.he.attend.service.RuleService;
import com.he.attend.service.StaffService;
import com.wangfan.endecrypt.utils.EndecryptUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("${api.version}/staff")
public class StaffController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private AttendService attendService;

    @Autowired
    private RuleService ruleService;
    @RequestMapping("/insert")
    public PageResult insertStaff(Staff staff, HttpServletRequest request){
        String authority="post:/staff/insert";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return new PageResult("没有相应的权限",400);
        }
        if(staffService.insert(staff)){
            return new PageResult("添加成功",200);
        }
        return new PageResult("添加失败",400);
    }

    @RequestMapping("/update")
    public PageResult updateStaff(Staff staff,HttpServletRequest request){
        String authority="put:/staff/update";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return new PageResult("没有相应的权限",400);
        }
        if(staffService.updateById(staff)){
            return new PageResult("修改成功",200);
        }
        return new PageResult("修改失败",400);
    }

    @RequestMapping("/delete/{staffId}")
    public PageResult delteStaff(@PathVariable("staffId") Integer staffId,HttpServletRequest request){
        String authority="delete:/staff/delete";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return new PageResult("没有相应的权限",400);
        }
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
    public JsonResult updateState(Integer staffId, Integer state,HttpServletRequest request) {
        String authority="put:/staff/statu";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return JsonResult.error("没有相应的权限");
        }
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

    @ApiOperation(value = "重置密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @PutMapping("/psw/{id}")
    public JsonResult resetPsw(@PathVariable("id") Integer staffId,HttpServletRequest request) {
        String authority="put:/staff/psw/{id}";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return JsonResult.error("没有相应的权限");
        }
        Staff staff = new Staff();
        staff.setStaffId(staffId);
        staff.setPassword(("123456"));
        if (staffService.updateById(staff)) {
            return JsonResult.ok("重置密码成功");
        }
        return JsonResult.error("重置密码失败");
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
        return new PageResult<>(0,"查询员工成功",staffList.size(),staffList);
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


    /**
     * 查询今日打卡和未打卡人数
     */
    @RequestMapping("/queryAttendAndNotAttend")
    public JSONObject queryAttendAndNotAttend(){
        //所有员工人数
        Integer staffNum=staffService.queryStaffNum();
        List<Attend> todayAttend=attendService.queryTodayAttend();
        List<Integer> havaAttendStaffIds=new ArrayList<>();//已打卡员工的id
        for(Attend attend:todayAttend){
            if(!havaAttendStaffIds.contains(attend.getStaffId())){
                havaAttendStaffIds.add(attend.getStaffId());
            }
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("attendnum",havaAttendStaffIds.size());
        jsonObject.put("notattendnum",staffNum-havaAttendStaffIds.size());
        return jsonObject;
    }

    /**
     * 查询今日打卡各个类型的人数
     */
    @RequestMapping("/queryAttendTypeNum")
    public JSONObject queryAttendTypeNum(){
        //所有员工人数
        Integer staffNum=staffService.queryStaffNum();
        List<Attend> todayAttend=attendService.queryTodayAttend();
        List<Integer> havaAttendStaffIds=new ArrayList<>();//已打卡员工的id
        Integer normal=0;
        Integer lateAttendNum=0;
        Integer earlyAttendNum=0;

        Integer inAttend=0;//内勤打卡人数
        Integer outAttend=0;//外勤打卡人数
        for(Attend attend:todayAttend){
            if(!havaAttendStaffIds.contains(attend.getStaffId())){
                havaAttendStaffIds.add(attend.getStaffId());
            }
            if(attend.getType().equals("正常")){
                normal+=1;
            }else if(attend.getType().equals("迟到")){
                lateAttendNum+=1;
            }else if(attend.getType().equals("早退")){
                earlyAttendNum+=1;
            }

            if(attend.getPlaceType().equals("内勤")){
                inAttend+=1;
            }else {
                outAttend+=1;
            }
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("normal",normal);
        jsonObject.put("lateAttendNum",lateAttendNum);
        jsonObject.put("earlyAttendNum",earlyAttendNum);
        jsonObject.put("inAttend",inAttend);
        jsonObject.put("outAttend",outAttend);
        jsonObject.put("notattendnum",staffNum-havaAttendStaffIds.size());
        return jsonObject;
    }
}
