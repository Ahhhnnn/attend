package com.he.attend.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.DayReportMapper;
import com.he.attend.dao.MonthReportMapper;
import com.he.attend.model.DayReport;
import com.he.attend.model.MonthReport;
import com.he.attend.service.DayReportService;
import com.he.attend.service.MonthReportService;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.List;

@Service
public class MonthReportServiceImpl extends ServiceImpl<MonthReportMapper, MonthReport> implements MonthReportService {

    @Override
    public List<MonthReport> queryByMonth(String month) {
        EntityWrapper<MonthReport> monthReportEntityWrapper=new EntityWrapper<MonthReport>();
        monthReportEntityWrapper.like("month",month);
        return baseMapper.selectList(monthReportEntityWrapper);
    }
}
