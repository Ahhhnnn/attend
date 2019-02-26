package com.he.attend.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.CalendarMapper;
import com.he.attend.dao.PlaceMapper;
import com.he.attend.model.AttendCalendar;
import com.he.attend.model.Place;
import com.he.attend.service.CalendarService;
import com.he.attend.service.PlaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarServiceImpl extends ServiceImpl<CalendarMapper, AttendCalendar> implements CalendarService {

    @Override
    public List<Integer> queryStaffIds() {
        return baseMapper.queryStaffIds();
    }

    @Override
    public List<AttendCalendar> queryCalendarByStaffid(Integer staffId) {
        return baseMapper.queryCalendarByStaffid(staffId);
    }
}
