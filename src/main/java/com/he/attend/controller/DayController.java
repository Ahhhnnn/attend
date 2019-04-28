package com.he.attend.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.PageResult;
import com.he.attend.model.Attend;
import com.he.attend.model.DayReport;
import com.he.attend.model.Staff;
import com.he.attend.service.AttendService;
import com.he.attend.service.DayReportService;
import com.he.attend.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author he
 * @title: DayController
 * @projectName attendhn
 * @description: TODO
 * @date 2019/4/2121:32
 */
@RestController
@RequestMapping("${api.version}/day")
public class DayController {
    @Autowired
    private DayReportService dayService;

    @Autowired
    private AttendService attendService;
    @Autowired
    private StaffService staffService;

    @RequestMapping("/query")
    public PageResult<DayReport> list(Integer page, Integer limit, String searchKey, String searchValue, String day) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }

        //先插入日报数据

        Page<DayReport> dayPage = new Page<>(page, limit);
        EntityWrapper<DayReport> wrapper = new EntityWrapper<>();
        if (searchKey != null && !searchKey.trim().isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
            wrapper.eq(searchKey, searchValue);
            wrapper.orderBy("create_time", true);
        }
        dayService.selectPage(dayPage, wrapper);
        List<DayReport> daysList = dayPage.getRecords();
        return new PageResult<>(daysList, dayPage.getTotal());
    }

    private void setDayReport(String day){
        EntityWrapper<Attend> entityWrapper=new EntityWrapper<>();
        entityWrapper.like("attend_time",day);
        List<Attend> attendList=attendService.selectList(entityWrapper);//查询今天所有的打卡记录
        //查询出所有的人员
        EntityWrapper<Staff> staffWrapper=new EntityWrapper<Staff>();
        staffWrapper.eq("state",0);
        List<Staff> staffList=staffService.selectList(staffWrapper);

    }
}
