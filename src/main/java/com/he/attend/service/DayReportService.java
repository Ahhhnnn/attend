package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.DayReport;
import com.he.attend.model.User;

import java.util.List;

public interface DayReportService extends IService<DayReport> {


    List<DayReport> queryByDay(String day);
}
