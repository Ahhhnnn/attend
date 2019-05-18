package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.Attend;
import com.he.attend.model.Staff;

import java.util.List;

public interface AttendService extends IService<Attend> {

    void logicDelete(Integer attendId);
    List<Attend> queryByStaffIdAndAttendDay(Integer staffId, String attendDay);
    List<Attend> queryTodayAttend();
}
