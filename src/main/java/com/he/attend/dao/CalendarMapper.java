package com.he.attend.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.he.attend.model.AttendCalendar;
import com.he.attend.model.Place;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CalendarMapper extends BaseMapper<AttendCalendar> {
    List<Integer> queryStaffIds();

    List<AttendCalendar> queryCalendarByStaffid(@Param("staffid") Integer staffid);

}
