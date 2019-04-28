package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.AttendCalendar;
import com.he.attend.model.Dept;

import java.util.List;

public interface CalendarService extends IService<AttendCalendar> {

        List<Integer> queryStaffIds();
        List<AttendCalendar> queryCalendarByStaffid(Integer staffId);
        boolean insert(List<Integer> staffIds,List<String> attendDays,Integer placeId,String placeName,Integer shiftId);
        boolean delete(List<Integer> staffIds,List<String> attendDays);
}
