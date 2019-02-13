package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.sql.Date;


@Data
@TableName("ts_shift")
public class Shift {
    private int shiftId;//班次id
    private String shiftName;//班次名称
    private String beginTime;//班次开始时间
    private String endTime;//班次结束时间
    private int state;//状态
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
}