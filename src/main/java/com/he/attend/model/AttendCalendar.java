package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ts_staff_calendar")
public class AttendCalendar {
    @TableId
    private Integer id;//主键id

    private Integer placeId;//考勤地点id，外键

    private String placeName;//考勤地点名称

    private Integer staffId;//人员id

    private String staffName;//人员姓名

    private Integer shiftId;//班次id

    private String shiftName;//班次名称

    private String day;//排班日期

    private Integer statu;//状态

    private Date createTime;  // 注册时间

    private Date updateTime;  // 修改时间
}
