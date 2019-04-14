package com.he.attend.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.PageResult;
import com.he.attend.common.utils.DataUtils;
import com.he.attend.model.Dept;
import com.he.attend.model.Rule;
import com.he.attend.model.Shift;
import com.he.attend.model.Staff;
import com.he.attend.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.spi.StateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * rule Controller
 * hening
 */
@Slf4j
@RestController
@RequestMapping("${api.version}/rule")
public class RuleController {
    @Autowired
    private ShiftService shiftService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private CalendarService calendarService;

    /**
     * 新增一个rule
     * @param rule 实体
     * @return
     */
    @RequestMapping("insert")
    public PageResult insert(Rule rule){

        //通过班次id查询班次，设置rule中的班次名称
        Shift shift=shiftService.getShiftById(rule.getShiftId());
        rule.setShiftName(shift.getShiftName());
        //通过placeid查询考勤地点，设置rule中的考勤地点名称
        String placeName=placeService.queryPlaceNameById(rule.getPlaceId());
        rule.setPlaceName(placeName);
        //TODO
        //新增考勤规则时，对传入的部门和人员 在开始时间至结束时间排班
        String attendDays=rule.getDays();
        String startDay=attendDays.split(" - ")[0];
        String endDay=attendDays.split(" - ")[1];
        List<String> days=DataUtils.getDays(startDay,endDay);//需要排班的日期的集合
        //通过传入的部门id 和 人员id 去重，决定要给那些人员排班
        List<Integer> staffIds=getFinalStaffIds(rule.getDeptIds(),rule.getStaffIds());
        if(!calendarService.insert(staffIds,days,rule.getPlaceName(),rule.getShiftId())){
            return new PageResult("排班失败",400);
        }
        ruleService.insert(rule);

        return new PageResult("新增且排班成功",200);
    }


    @ApiOperation(value = "查询所有考勤规则", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "limit", value = "每页多少条", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "searchKey", value = "筛选条件字段", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "searchValue", value = "筛选条件关键字", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @RequestMapping("query")
    public PageResult<Rule> list(Integer page, Integer limit, String searchKey, String searchValue) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }
        Page<Rule> rulePage = new Page<>(page, limit);
        EntityWrapper<Rule> wrapper = new EntityWrapper<>();
        if (searchKey != null && !searchKey.trim().isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
            if(searchKey.equals("staff_name")||searchKey.equals("phone")){
                wrapper.like(searchKey,searchValue);
            }else {
                wrapper.eq(searchKey, searchValue);
            }
        }
        wrapper.orderBy("create_time", true);
        ruleService.selectPage(rulePage, wrapper);
        List<Rule> ruleList = rulePage.getRecords();
        //取出staffids 和 deptids 查询staff 和dept实体 赋值为rule
        for(Rule rule:ruleList){
            String deptIds=rule.getDeptIds();
            String staffIds=rule.getStaffIds();
            //如果不为空 通过逗号分隔 变为list    将String 变为 int 存入list中
            List<Integer> deptIdList=new ArrayList<>();
            List<Integer> staffIdList=new ArrayList<>();
            if(!deptIds.equals("")) {
                for (String deptId : deptIds.split(",")) {
                    deptIdList.add(Integer.valueOf(deptId));
                }
                EntityWrapper<Dept> deptwapper=new EntityWrapper<Dept>();
                deptwapper.in("dept_id",deptIdList);
                List<Dept> deptList=deptService.selectList(deptwapper);
                rule.setDepts(deptList);
            }
            if(!staffIds.equals("")){
                for(String staffid:staffIds.split(",")){
                    staffIdList.add(Integer.valueOf(staffid));
                }
                //通过staffids  查询
                EntityWrapper<Staff> staffwapper=new EntityWrapper<Staff>();
                staffwapper.in("staff_id",staffIdList);
                List<Staff> staffList=staffService.selectList(staffwapper);
                rule.setStaffs(staffList);
            }
        }
        return new PageResult<>(0,"成功",ruleList.size(),ruleList);
    }

    @RequestMapping("delete/{ruleId}")
    public PageResult deleteById(@PathVariable("ruleId") Integer ruleId){

        ruleService.deleteById(ruleId);
        return new PageResult("删除成功",200);
    }

    @RequestMapping("update")
    public PageResult updateRule(Rule rule){
        //通过班次id查询班次，设置rule中的班次名称
        Shift shift=shiftService.getShiftById(rule.getShiftId());
        rule.setShiftName(shift.getShiftName());
        //通过placeid查询考勤地点，设置rule中的考勤地点名称
        String placeName=placeService.queryPlaceNameById(rule.getPlaceId());
        rule.setPlaceName(placeName);
        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("rule_id",rule.getRuleId());
        String attendDays=rule.getDays();
        String startDay=attendDays.split(" - ")[0];
        String endDay=attendDays.split(" - ")[1];
        List<String> days=DataUtils.getDays(startDay,endDay);//需要排班的日期的集合
        //TODO
        //重新排班，将修改后的开始至结束时间 所有人人员的排班删除后在重新插入
        List<Integer> staffIds=getFinalStaffIds(rule.getDeptIds(),rule.getStaffIds());
        if(calendarService.delete(staffIds,days)){//如果删除成功 就重新插入排班
            if(!calendarService.insert(staffIds,days,rule.getPlaceName(),rule.getShiftId())){
                return new PageResult("排班失败",400);
            }
        }else {//如果删除失败 返回排班失败
            return new PageResult("排班失败",400);
        }
        if(ruleService.update(rule,entityWrapper)){
            return new PageResult("修改成功",200);
        }
        return new PageResult("修改失败",400);
    }

    /**
     * 公用方法，得到去重后的staffids
     * @param deptId 部门id
     * @param staffId 人员id
     * TODO
     * @return
     */
    public List<Integer> getFinalStaffIds(String deptId,String staffId){
        List<Integer> deptIds=new ArrayList<>();
        List<Integer> staffIds=new ArrayList<>();
        List<Integer> finalStaffIds=new ArrayList<>();
        List<Integer> staffIdsInDepts=new ArrayList<>();
        for(String deptid:deptId.split(",")){
            deptIds.add(Integer.valueOf(deptid));
        }
        for(String staffid:staffId.split(",")){
            staffIds.add(Integer.valueOf(staffid));
        }
        //获取部门下的所有人员id
        for(Integer deptid:deptIds){
            List<Staff> staffList=staffService.queryByDeptId(deptid);
            for(Staff staff:staffList){
                staffIdsInDepts.add(staff.getStaffId());
            }
        }
        //去重 获取需要排班的人员id
        finalStaffIds.addAll(staffIdsInDepts);
        for(Integer staffid:staffIds){
            if(!finalStaffIds.contains(staffid)){
                finalStaffIds.add(staffid);
            }
        }
        return finalStaffIds;
    }
}
