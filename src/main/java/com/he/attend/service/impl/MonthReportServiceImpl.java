package com.he.attend.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.DayReportMapper;
import com.he.attend.dao.MonthReportMapper;
import com.he.attend.model.DayReport;
import com.he.attend.model.MonthReport;
import com.he.attend.service.DayReportService;
import com.he.attend.service.MonthReportService;
import org.springframework.stereotype.Service;

@Service
public class MonthReportServiceImpl extends ServiceImpl<MonthReportMapper, MonthReport> implements MonthReportService {

}
