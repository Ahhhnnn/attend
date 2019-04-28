package com.he.attend.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.DayReportMapper;
import com.he.attend.dao.UserMapper;
import com.he.attend.model.DayReport;
import com.he.attend.model.Staff;
import com.he.attend.model.User;
import com.he.attend.service.AttendService;
import com.he.attend.service.DayReportService;
import com.he.attend.service.StaffService;
import com.he.attend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DayReportServiceImpl extends ServiceImpl<DayReportMapper, DayReport> implements DayReportService {

    @Autowired
    private StaffService staffService;

    @Autowired
    private AttendService attendService;

}
