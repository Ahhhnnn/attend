package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.AttendCalendar;
import com.he.attend.model.Dept;

import java.util.List;

public interface CalendarService extends IService<AttendCalendar> {

        List<Integer> queryStaffIds();
        List<AttendCalendar> queryCalendarByStaffid(Integer staffId);
}
