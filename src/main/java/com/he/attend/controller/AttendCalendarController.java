package com.he.attend.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.he.attend.common.PageResult;
import com.he.attend.model.AttendCalendar;
import com.he.attend.model.Staff;
import com.he.attend.service.CalendarService;
import com.he.attend.service.DeptService;
import com.he.attend.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("${api.version}/calendar")
public class AttendCalendarController {
    @Autowired
    private DeptService deptService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private CalendarService calendarService;
    @RequestMapping("queryCalendars")
    public JSONObject query(Integer staffId,Integer deptId){
        //如果传入部门id不为空
        if(deptId!=null) {
            EntityWrapper<Staff> wrapper = new EntityWrapper<>();
            wrapper.eq("dept_id", deptId);
            //查询该部门下的人员
            List<Staff> staffList = staffService.selectList(wrapper);
            List<Integer> staffids=staffList.stream().map(staff -> staff.getStaffId()).collect(Collectors.toList());
            //查询人员的排班
            List<AttendCalendar> calendars=calendarService.selectList(new EntityWrapper().in("staff_id",staffids));
            PageResult<AttendCalendar> pageResult=new PageResult<>(calendars,calendars.size());
            JSONObject jsonObject=new JSONObject();
            return jsonObject;
        }
        //如果部门id为空,查询所有人员的排班记录
        //List<Integer> staffids=Arrays.asList(staffIds);
        List<Integer> staffids =staffService.selectList(null).stream().map(staff -> staff.getStaffId()).collect(Collectors.toList());
        EntityWrapper<AttendCalendar> staffwrapper = new EntityWrapper<>();
        staffwrapper.in("staff_id",staffids);
        List<AttendCalendar> calendars=calendarService.selectList(staffwrapper);
        List<Integer> staffIds=calendars.stream().map(attendCalendar -> attendCalendar.getStaffId()).collect(Collectors.toList());
        //获取所有人员id  去重
       /* List<Integer> staffIdsNotSame=calendarService.queryStaffIds();*/
        List<Integer> staffIdsNotSame=new ArrayList<>();
        for(Integer staffid:staffIds){
            if(!staffIdsNotSame.contains(staffid)){
                staffIdsNotSame.add(staffid);
            }
        }


        //拼装json对象
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code",0);
        jsonObject.put("msg","操作成功");
        jsonObject.put("count",calendars.size());
        jsonObject.put("data",calendars);

        //PageResult<AttendCalendar> pageResult=new PageResult<>(calendars,calendars.size());

        return jsonObject;
    }

    @RequestMapping("/queryCalendar")
    public PageResult queryCalendar(Integer staffId,String date){

        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("staff_id",staffId);
        entityWrapper.eq("day",date);

        List<AttendCalendar> attendCalendars= calendarService.selectList(entityWrapper);
        if(!CollectionUtils.isEmpty(attendCalendars)){//不为空说明 用户今天有排班
            return new PageResult(200,"success",attendCalendars.size(),attendCalendars);
        }
        //若为空说明用户今天无排班
        return new  PageResult("用户今日无排班",400);
    }




}
