package com.he.attend.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.PageResult;
import com.he.attend.common.utils.DateUtil;
import com.he.attend.common.utils.StringUtil;
import com.he.attend.model.*;
import com.he.attend.service.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author duhan
 * @title: MonthController
 * @projectName attendhn
 * @description: TODO
 * @date 2019/4/2121:19
 */
@Slf4j
@RestController
@RequestMapping("${api.version}/month")
public class MonthController {
    @Autowired
    private MonthReportService monthService;
    @Autowired
    private AttendService attendService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private CalendarService calendarService;
    
    @RequestMapping("/query")
    public PageResult<MonthReport> list(Integer page, Integer limit, String searchKey, String searchValue,String month) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }

        if(month==null){
            Date today=new Date();
            month=DateUtil.formatDate(today,"yyyy-MM");
        }
        //先插入月报数据
        //TODO
        //查询是否已有月报 如果有则直接查询 ，如果没有 查询后查询
        List<MonthReport> monthReportList=monthService.queryByMonth(month);
        if(CollectionUtils.isEmpty(monthReportList)){
            setMonthReport(month);
        }
        Page<MonthReport> monthPage = new Page<>(page, limit);
        EntityWrapper<MonthReport> wrapper = new EntityWrapper<>();
        if (searchKey != null && !searchKey.trim().isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
            wrapper.eq(searchKey, searchValue);
            wrapper.orderBy("create_time", true);
        }
        monthService.selectPage(monthPage, wrapper);
        List<MonthReport> monthsList = monthPage.getRecords();
        return new PageResult<>(monthsList, monthPage.getTotal());
    }

    @RequestMapping("/insert")
    public PageResult insertMonreport(String month){
        if(StringUtils.isEmpty(month)){
            Date today=new Date();
            month=DateUtil.formatDate(today,"yyyy-MM");
        }
        try {
            setMonthReport(month);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new PageResult("重新计算月报失败",400);
        }
        return new PageResult("计算月报成功",200);
    }

    private void setMonthReport(String month){
        //查询出所有的人员
        EntityWrapper<Staff> staffWrapper=new EntityWrapper<Staff>();
        staffWrapper.eq("state",0);
        List<Staff> staffList=staffService.selectList(staffWrapper);
        List<MonthReport> monthReportList=new ArrayList<>();
        for(Staff staff:staffList){
            EntityWrapper<Attend> entityWrapper=new EntityWrapper<>();
            entityWrapper.eq("staff_id",staff.getStaffId());
            entityWrapper.like("attend_time",month);
            //当前员工当月的所有考勤记录
            List<Attend> attendList=attendService.selectList(entityWrapper);
            MonthReport monthReport=new MonthReport();
            monthReport.setMonth(month);
            monthReport.setStaffId(staff.getStaffId());
            monthReport.setStaffName(staff.getStaffName());
            monthReport.setDeptId(staff.getDeptId());
            monthReport.setDeptName(staff.getDeptName());
            int lateNum=calculateNum(attendList,"迟到");
            int leaveEarlyNum=calculateNum(attendList,"早退");
            monthReport.setLateNum(lateNum);
            monthReport.setLeaveEarlyNum(leaveEarlyNum);

            //计算出勤工时 缺勤工时
            List<String> allInfoList=calculateWorkHour(staff,month);
            monthReport.setShoudWorkHour(allInfoList.get(0));
            monthReport.setWorkHour(allInfoList.get(1));
            monthReport.setNotWorkHour(allInfoList.get(2));
            monthReport.setNotWorkDay(Integer.valueOf(allInfoList.get(3)));

            //通过查询人员排班信息计算 应出勤工时
            monthReportList.add(monthReport);


        }

        //插入前先删除已有月报数据
        EntityWrapper<MonthReport> monthReportentityWrapper=new EntityWrapper<MonthReport>();
        monthReportentityWrapper.in("staff_id",staffList.stream().map(Staff->Staff.getStaffId()).collect(Collectors.toList()));
        monthReportentityWrapper.like("month",month);

        monthService.delete(monthReportentityWrapper);

        monthService.insertBatch(monthReportList);
    }

    /**
     * 计算次数
     * @param attendList  打卡记录
     * @param type 打卡类型 迟到 正常 早退
     * @return
     */
    private Integer calculateNum(List<Attend> attendList,String type){
        int num=0;
        for(Attend attend:attendList){
            if(attend.getType().equals(type)){
                num++;
            }
        }
        return num;
    }

    /**
     * 计算传入人员 在传入月份的
     * 应出勤工时，出勤工时，缺勤工时，旷工天数
     * @param staff 人员
     * @param month 月份
     * @return 返回list  0：应出勤工时 1：出勤工时， 2：缺勤工时 3 ：旷工天数
     */
    private List<String> calculateWorkHour(Staff staff,String month){
        int year=Integer.valueOf(month.split("-")[0]);
        int inmonth=Integer.valueOf(month.split("-")[1]);

        String startTime= DateUtil.getFitstDayOfMonth(year,inmonth);
        String endTime=DateUtil.getLastDayOfMonth(year,inmonth);
        List<String> days=DateUtil.getDays(startTime,endTime);//传入月份的每一天日期
        long shoudWorkHour=0;//应出勤工时
        long relWorkHour=0;//实际出勤工时
        long notWorkHour=0;//缺勤工时
        int notWorkNum=0;
        for(String day:days){
            EntityWrapper<Attend> entityWrapper=new EntityWrapper<Attend>();
            entityWrapper.eq("staff_id",staff.getStaffId());
            entityWrapper.like("attend_time",day);
            List<Attend> attendList=attendService.selectList(entityWrapper);

            if(attendList.size()>1){ //有两条打卡记录 打卡正常
                String firstAttend=attendList.get(0).getAttendTime();
                String secAttend=attendList.get(1).getAttendTime();
                EntityWrapper<AttendCalendar> calendatWrapper=new EntityWrapper<AttendCalendar>();
                calendatWrapper.eq("day",day);
                calendatWrapper.eq("staff_id",staff.getStaffId());
                List<AttendCalendar> calendarList=calendarService.selectList(calendatWrapper);
                if(!CollectionUtils.isEmpty(calendarList)) {
                    AttendCalendar attendCalendar = calendarList.get(0);//有打卡记录就一定有排班
                    Integer shiftId = attendCalendar.getShiftId();
                    Shift shift = shiftService.getShiftById(shiftId);

                    long hour = DateUtil.dateDiff(shift.getBeginTime(), shift.getEndTime(), "HH:mm:ss", "h");//应该工作工时
                    long relHour = DateUtil.dateDiff(firstAttend, secAttend, "yyyy-MM-dd HH:mm:ss", "h");
                    long notHour = 0;
                    if (hour > relHour) {
                        notHour = hour - relHour;
                    } else {
                        notHour = 0;
                    }

                    relWorkHour += relHour;
                    notWorkHour += notHour;
                }
            }else if(attendList.size()==1){//
                //一次打卡 缺考勤


            }else if(attendList.size()==0){
                //0次打卡视为旷工
                notWorkNum+=1;
            }

        }
        //计算应出勤工时
        EntityWrapper<AttendCalendar> calendatWrapper=new EntityWrapper<AttendCalendar>();
        calendatWrapper.like("day",month);
        calendatWrapper.eq("staff_id",staff.getStaffId());
        List<AttendCalendar> calendarList=calendarService.selectList(calendatWrapper);
        for(AttendCalendar attendCalendar :calendarList){
            Integer shiftId=attendCalendar.getShiftId();
            Shift shift=shiftService.getShiftById(shiftId);
            long hour= DateUtil.dateDiff(shift.getBeginTime(),shift.getEndTime(),"HH:mm:ss","h");//应该工作工时
            shoudWorkHour+=hour;
        }

        List<String> allList=new ArrayList<>();
        allList.add(String.valueOf(shoudWorkHour));
        allList.add(String.valueOf(relWorkHour));
        allList.add(String.valueOf(notWorkHour));
        allList.add(String.valueOf(notWorkNum));
        return allList;
    }
}
