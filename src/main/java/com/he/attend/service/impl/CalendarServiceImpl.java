package com.he.attend.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.CalendarMapper;
import com.he.attend.dao.PlaceMapper;
import com.he.attend.model.*;
import com.he.attend.service.CalendarService;
import com.he.attend.service.PlaceService;
import com.he.attend.service.ShiftService;
import com.he.attend.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class CalendarServiceImpl extends ServiceImpl<CalendarMapper, AttendCalendar> implements CalendarService {

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private CalendarService calendarService;
    @Override
    public List<Integer> queryStaffIds() {
        return baseMapper.queryStaffIds();
    }

    @Override
    public List<AttendCalendar> queryCalendarByStaffid(Integer staffId) {
        return baseMapper.queryCalendarByStaffid(staffId);
    }

    /**
     * 给员工新增排班
     * @param staffIds 人员id
     * @param attendDays 排班日期
     * @param placeName 考勤地点
     * @param shiftId 班次名称
     * @return
     */
    @Override
    public boolean insert(List<Integer> staffIds, List<String> attendDays,Integer placeId, String placeName, Integer shiftId,Integer ruleId) {
        Long startTime=System.currentTimeMillis();
        try {
            Shift shift=shiftService.getShiftById(shiftId);
            List<AttendCalendar> calendarList=new ArrayList<>();
            List<Staff> staffList=staffService.queryByStaffIds(staffIds);
            for (Staff staff:staffList){
                for (String attendDay:attendDays){
                    AttendCalendar calendar=new AttendCalendar();
                    calendar.setRuleId(ruleId);
                    calendar.setPlaceId(placeId);
                    calendar.setPlaceName(placeName);
                    calendar.setStaffId(staff.getStaffId());
                    calendar.setStaffName(staff.getStaffName());
                    calendar.setShiftId(shiftId);
                    calendar.setShiftName(shift.getShiftName());
                    calendar.setDay(attendDay);
                    calendarList.add(calendar);
                }
            }
            calendarService.insertBatch(calendarList);
            Long endTime=System.currentTimeMillis();
            log.error("新增排班耗时为:"+(endTime-startTime));
        }catch (Exception e)
        {
            log.error(e.getMessage(),e);
            return false;
        }
        return true;
    }

    /**
     * 根据人员id 和 排班日期 删除员工当天的排班
     * @param staffIds
     * @param attendDays
     * @returna
     */
    @Override
    public boolean delete(List<Integer> staffIds, List<String> attendDays) {
        Long startTime=System.currentTimeMillis();
        List<AttendCalendar> calendars=new ArrayList<>();
        List<Integer> calendarIds=new ArrayList<>();
        try {
            /*for(Integer staffId : staffIds){
                for (String attendDay:attendDays){
                    EntityWrapper<AttendCalendar> entityWrapper=new EntityWrapper<AttendCalendar>();
                    entityWrapper.eq("staff_id",staffId);
                    entityWrapper.eq("day",attendDay);
                    calendars=calendarService.selectList(entityWrapper);
                }
            }*/
            EntityWrapper<AttendCalendar> entityWrapper=new EntityWrapper<AttendCalendar>();
            entityWrapper.in("staff_id",staffIds);
            entityWrapper.in("day",attendDays);
            calendars=calendarService.selectList(entityWrapper);
            for (AttendCalendar calendar:calendars){
                calendarIds.add(calendar.getId());
            }
            calendarService.deleteBatchIds(calendarIds);
        Long endTime=System.currentTimeMillis();
            log.error("删除排班耗时为:"+(endTime-startTime));
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return false;
        }
        return true;
    }
}
