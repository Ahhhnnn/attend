package com.he.attend.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.he.attend.common.PageResult;
import com.he.attend.model.MonthReport;
import com.he.attend.service.AttendService;
import com.he.attend.service.MonthReportService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author duhan
 * @title: MonthController
 * @projectName attendhn
 * @description: TODO
 * @date 2019/4/2121:19
 */
@RestController
@RequestMapping("${api.version}/month")
public class MonthController {
    @Autowired
    private MonthReportService monthService;
    @Autowired
    private AttendService attendService;
    
    @RequestMapping("/query")
    public PageResult<MonthReport> list(Integer page, Integer limit, String searchKey, String searchValue,String month) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 10;
        }

        //先插入月报数据
        Page<MonthReport> monthPage = new Page<>(page, limit);
        EntityWrapper<MonthReport> wrapper = new EntityWrapper<>();
        if (searchKey != null && !searchKey.trim().isEmpty() && searchValue != null && !searchValue.trim().isEmpty()) {
            wrapper.eq(searchKey, searchValue);
            wrapper.orderBy("create_time", true);
        }
        monthService.selectPage(monthPage, wrapper);
        List<MonthReport> monthsList = monthPage.getRecords();
        return new PageResult<>(monthsList, monthPage.getTotal());
    }
}
