package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.DayReport;
import com.he.attend.model.MonthReport;

import java.util.List;

public interface MonthReportService extends IService<MonthReport> {

        List<MonthReport> queryByMonth(String month);
}
