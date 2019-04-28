package com.he.attend.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.JsonResult;
import com.he.attend.common.PageResult;
import com.he.attend.model.Attend;
import com.he.attend.model.Dept;
import com.he.attend.model.Rule;
import com.he.attend.model.Staff;
import com.he.attend.service.DeptService;
import com.he.attend.service.RuleService;
import com.he.attend.service.StaffService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.digester.Rules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("${api.version}/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;
    @Autowired
    private StaffService staffService;

    @Autowired
    private RuleService ruleService;
    /**
     * 新增部门
     * @param dept
     * @return
     */
    @RequestMapping(value = "/insert" )
    public PageResult insertDept(Dept dept){

        if(checkDeptIsExist(dept,0)){
            return new PageResult("该部门编号或部门名已存在",400);
        }
        if(deptService.insert(dept)){
            return new PageResult("新增成功",200);
        }
        return new PageResult("新增失败",400);
    }

    @ApiOperation(value = "修改部门状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deptId", value = "部门id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "statu", value = "状态：0正常，1冻结", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @PutMapping("/statu")
    public JsonResult updateState(Integer deptId, Integer statu) {
        if (statu == null || (statu != 0 && statu != 1)) {
            return JsonResult.error("state值需要在[0,1]中");
        }
        try {
            deptService.updateStatu(deptId,statu);
            return JsonResult.ok();
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return JsonResult.error();
        }
    }

    /**
     * 更新部门信息
     * @param dept
     * @return
     */
    @RequestMapping(value = "/update" )
    public PageResult updateDept(Dept dept){

        if(checkDeptIsExist(dept,1)){
            return new PageResult("该部门编号或部门名已存在",400);
        }
        if(deptService.updateById(dept)){
            return new PageResult("修改成功",200);
        }
        return new PageResult("修改失败",400);
    }

    /**
     * 根据部门id 删除部门
     * @param deptId
     * @return
     */
    @RequestMapping("/delete/{deptId}")
    public PageResult delteDept(@PathVariable("deptId") Integer deptId){
        try {
            deptService.logicDelete(deptId);
            return new PageResult("删除成功",200);
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return new PageResult("删除失败", 400);
        }
    }
    /**
     * 查询所有部门
     * @return
     */
    @RequestMapping("/queryAll")
    public PageResult<Dept> queryAll(){
        EntityWrapper<Dept> wrapper = new EntityWrapper<>();
        wrapper.eq("dr",0);
        wrapper.orderBy("create_time");
        List<Dept> deptList= deptService.selectList(wrapper);
        PageResult<Dept> pageResult=new PageResult(200,"操作成功",deptList.size(),deptList);
        return pageResult;
    }

    /**
     * 分页查询部门
     * @param page
     * @param limit
     * @param searchKey
     * @param searchValue
     * @return
     */
    @ApiOperation(value = "查询部门", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @RequestMapping("/query")
    public PageResult<Dept> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }
        Page<Dept> deptPage = new Page<>(page, limit);
        EntityWrapper<Dept> wrapper = new EntityWrapper<>();
        if (searchKey != null && !searchKey.trim().isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
            if(searchKey.equals("dept_name")){
                wrapper.like(searchKey,searchValue);
            }else {
                wrapper.eq(searchKey, searchValue);
            }
        }
        wrapper.eq("dr",0);
        wrapper.orderBy("create_time", true);
        deptService.selectPage(deptPage, wrapper);
        List<Dept> deptsList = deptPage.getRecords();
        return new PageResult<>(deptsList, deptPage.getTotal());
    }

    /**
     * 根据deptId查询部门人员
     * @param page
     * @param limit
     * @param deptId
     * @return
     */
    @RequestMapping("/queryByDeptId")
    public PageResult<Staff> queryByDeptId(Integer page, Integer limit,Integer deptId){
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }
        Page<Staff> staffPage = new Page<>(page, limit);
        EntityWrapper<Staff> wrapper = new EntityWrapper<>();
        wrapper.eq("dept_id",deptId);
        wrapper.orderBy("create_time", true);
        staffService.selectPage(staffPage,wrapper);
        List<Staff> staffsList = staffPage.getRecords();
        return new PageResult<>(staffsList, staffPage.getTotal());
    }
    /**
     * 检查部门是否已存在
     * @param dept
     * @param isInsetOrUpdate 是新增还是更新  0:新增1:更新
     * @return
     */
    private boolean checkDeptIsExist(Dept dept,Integer isInsetOrUpdate){
        EntityWrapper<Dept> wrapper = new EntityWrapper<>();
        wrapper.eq("dr",0);
        List<Dept> depts=deptService.selectList(wrapper);

        for (Dept dept1:depts){
            if(dept.getDeptId().equals(dept1.getDeptId())){
               return true;
            }
            //如果是新增部门判断是否名称相同
            if(isInsetOrUpdate==0) {
                if (dept.getDeptName().equals(dept1.getDeptName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 查询所有状态正常且未删除的部门
     * @return
     */
    @RequestMapping("/queryAllStatu")
    public PageResult<Dept> queryAllStatu(){
        EntityWrapper entity=new EntityWrapper();
        entity.eq("dr",0);
        entity.eq("statu",0);
        List<Dept> depts=deptService.selectList(entity);
        return new PageResult<>(200,"查询成功",depts.size(),depts);
    }


    /**
     * 新增规则 调用 去重的部门
     * @return
     */
    @RequestMapping("/queryAllNotRepact")
    public PageResult<Dept> queryAllNotRepact(){
        EntityWrapper entity=new EntityWrapper();
        entity.eq("dr",0);
        entity.eq("statu",0);
        List<Dept> depts=deptService.selectList(entity);
        List<Rule> ruleList=ruleService.query();
        List<String> deptIdsString=ruleList.stream().map(Rule->Rule.getDeptIds()).collect(Collectors.toList());
        List<Integer> deptIds=new ArrayList<>();
        for(String deptId:deptIdsString){
            if(!deptId.equals("")) {
                for (String deptid : deptId.split(",")) {
                    deptIds.add(Integer.valueOf(deptid));
                }
            }
        }
        List<Dept> finalDepts=new ArrayList<>();
        finalDepts.addAll(depts);
        //去重
        for(Dept dept:depts){
            if(deptIds.contains(dept.getDeptId())){
                finalDepts.remove(dept);
            }
        }
        return new PageResult<>(200,"查询成功",finalDepts.size(),finalDepts);
    }
}
