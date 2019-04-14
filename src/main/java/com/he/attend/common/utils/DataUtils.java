package com.he.attend.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author he
 * @title: test
 * @projectName attendhn
 * @description: TODO
 * @date 2019/4/1217:19
 */
public  class DataUtils {


    /**
     *传入开始时间和结束时间，返回之间的所有日期的集合
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return
     */
    public static List<String> getDays(String startTime,String endTime){
        // 返回的日期集合
        List<String> days = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }
}
