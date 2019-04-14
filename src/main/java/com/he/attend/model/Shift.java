package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("ts_shift")
public class Shift {
    @TableId
    private Integer shiftId;//班次id
    private String shiftName;//班次名称
    private String color;//班次颜色
    private String beginTime;//班次开始时间
    private String endTime;//班次结束时间
    private Integer state;//状态
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
}