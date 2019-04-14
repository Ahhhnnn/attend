package com.he.attend.controller.AppCon;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.he.attend.common.PageResult;
import com.he.attend.common.utils.DateUtil;
import com.he.attend.model.AttendCalendar;
import com.he.attend.model.Shift;
import com.he.attend.service.CalendarService;
import com.he.attend.service.ShiftService;
import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController()
@RequestMapping("/appCalendar")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;

    @Autowired
    private ShiftService shiftService;

    @RequestMapping("/query")
    public PageResult queryCalendar(Integer staffId, String date){

        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("staff_id",staffId);
        entityWrapper.eq("day",date);

        List<AttendCalendar> attendCalendars= calendarService.selectList(entityWrapper);
        if(!CollectionUtils.isEmpty(attendCalendars)){//不为空说明 用户今天有排班
            return new PageResult(200,"success",attendCalendars.size(),attendCalendars);
        }
        //若为空说明用户今天无排班
        return new  PageResult("用户今日无排班",400);
    }


    @RequestMapping("/queryShift")
    public PageResult queryShift(Integer shiftId){

        EntityWrapper entityWrapper=new EntityWrapper();
        entityWrapper.eq("shift_id",shiftId);

        List<Shift> shifts= shiftService.selectList(entityWrapper);
        if(!CollectionUtils.isEmpty(shifts)){//不为空说明 有对应的班次
            return new PageResult(200,"查询成功",shifts.size(),shifts);
        }
        //为空说明无此班次
        return new  PageResult("无次班次信息",400);
    }

    /**
     * 排班界面展示 测试用
     * @return
     */
    @RequestMapping("querytest")
    public JSONObject queryTest(String startTime,String endTime){
        SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
        String start= DateUtil.getUnixToString(startTime,"yyyy-MM-dd");
        String end=DateUtil.getUnixToString(endTime,"yyyy-MM-dd");
        JSONObject jsonObject=new JSONObject();
        /*title: 'Business Lunch',
        start: '2017-10-03T13:00:00',
        constraint: 'businessHours'*/
        jsonObject.put("title","Business Lunch");
        jsonObject.put("start","2017-10-03T13:00:00");
        jsonObject.put("constraint","businessHours");
        return jsonObject;
    }
}
