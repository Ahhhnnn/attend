package com.he.attend.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.PageResult;
import com.he.attend.common.utils.DateUtil;
import com.he.attend.model.*;
import com.he.attend.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author he
 * @title: DayController
 * @projectName attendhn
 * @description: TODO
 * @date 2019/4/2121:32
 */
@Slf4j
@RestController
@RequestMapping("${api.version}/day")
public class DayController {
    @Autowired
    private DayReportService dayService;

    @Autowired
    private AttendService attendService;
    @Autowired
    private StaffService staffService;

    @Autowired
    private CalendarService calendarService;
    @Autowired
    private ShiftService shiftService;

    @Autowired
    private DayReportService dayReportService;
    @RequestMapping("/query")
    public PageResult<DayReport> list(Integer page, Integer limit, String searchKey, String searchValue, String day) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }

        if(day==null){
            Date today=new Date();
            day=DateUtil.formatDate(today,"yyyy-MM-dd");
        }
        //先插入日报数据
        List<DayReport> dayReportList=dayService.queryByDay(day);
        if(CollectionUtils.isEmpty(dayReportList)) {
            setDayReport(day);
        }
        Page<DayReport> dayPage = new Page<>(page, limit);
        EntityWrapper<DayReport> wrapper = new EntityWrapper<>();
        if (searchKey != null && !searchKey.trim().isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
            wrapper.eq(searchKey, searchValue);
            wrapper.orderBy("create_time", true);
        }
        wrapper.eq("day",day);
        dayService.selectPage(dayPage, wrapper);
        List<DayReport> daysList = dayPage.getRecords();
        return new PageResult<>(daysList, dayPage.getTotal());
    }

    @RequestMapping("/insert")
    public PageResult insertMonreport(String day){
        if(StringUtils.isEmpty(day)){
            Date today=new Date();
            day=DateUtil.formatDate(today,"yyyy-MM-dd");
        }
        try {
            setDayReport(day);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new PageResult("重新计算日报失败",400);
        }

        return new PageResult("计算日报成功",200);
    }

    private void setDayReport(String day){
        //查询出所有的人员
        EntityWrapper<Staff> staffWrapper=new EntityWrapper<Staff>();
        staffWrapper.eq("state",0);
        List<Staff> staffList=staffService.selectList(staffWrapper);
        List<DayReport> dayReportList=new ArrayList<>();
        for(Staff staff:staffList){
            EntityWrapper<Attend> entityWrapper=new EntityWrapper<>();
            entityWrapper.eq("staff_id",staff.getStaffId());
            entityWrapper.like("attend_time",day);
            List<Attend> attendList=attendService.selectList(entityWrapper);

            DayReport dayReport=new DayReport();
            dayReport.setDay(day);
            dayReport.setStaffId(staff.getStaffId());
            dayReport.setStaffName(staff.getStaffName());

            if(attendList.size()>1){ //有两条打卡记录 打卡正常
                String firstAttend=attendList.get(0).getAttendTime();
                String secAttend=attendList.get(1).getAttendTime();
                dayReport.setFirAttendTime(firstAttend);
                dayReport.setSecAttendTime(secAttend);
                EntityWrapper<AttendCalendar> calendatWrapper=new EntityWrapper<AttendCalendar>();
                calendatWrapper.eq("day",day);
                calendatWrapper.eq("staff_id",staff.getStaffId());
                List<AttendCalendar> calendarList=calendarService.selectList(calendatWrapper);
                AttendCalendar attendCalendar=calendarList.get(0);//有打卡记录就一定有排班
                Integer shiftId=attendCalendar.getShiftId();
                Shift shift=shiftService.getShiftById(shiftId);

                long hour= DateUtil.dateDiff(shift.getBeginTime(),shift.getEndTime(),"HH:mm:ss","h");//应该工作工时
                long relHour=DateUtil.dateDiff(firstAttend,secAttend,"yyyy-MM-dd HH:mm:ss","h");
                long notWorkHour=0;
                if(hour>relHour){
                    notWorkHour=hour-relHour;
                }else {
                    notWorkHour=0;
                }
                dayReport.setWorkHour(String.valueOf(relHour));
                dayReport.setNotWorkHour(String.valueOf(notWorkHour));

                dayReportList.add(dayReport);

            }else {//打卡不正常
                dayReport.setFirAttendTime("");
                dayReport.setSecAttendTime("");
                dayReport.setWorkHour("打卡数据不足");
                dayReport.setNotWorkHour("打卡数据不足");

                dayReportList.add(dayReport);

            }

        }
        EntityWrapper<DayReport> dayReportentityWrapper=new EntityWrapper<DayReport>();
        dayReportentityWrapper.in("staff_id",staffList.stream().map(Staff->Staff.getStaffId()).collect(Collectors.toList()));
        dayReportentityWrapper.like("day",day);
        dayReportService.delete(dayReportentityWrapper);//插入前先删除
        dayReportService.insertBatch(dayReportList);
    }
}
