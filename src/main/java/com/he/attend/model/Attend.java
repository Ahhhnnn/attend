package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.sql.Date;


@Data
@TableName("ts_attend")
public class Attend {
    private int attendId;//考勤记录id
    private int staffId;//人员id
    private String staffName;//人员名称
    private int shiftId;//班次id
    private String shiftName;//班次名称
    private int type; //考勤类型 1 正常  2 迟到 3 早退 4 请假
    private String place;//打卡地点
    private Date attendTime;//打卡时间
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
}