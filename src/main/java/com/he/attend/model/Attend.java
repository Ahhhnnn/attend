package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.sql.Date;


@Data
@TableName("ts_attend")
public class Attend {
    @TableId
    private Integer attendId;//考勤记录id
    private Integer staffId;//人员id
    private String staffName;//人员名称
    private Integer shiftId;//班次id
    private String shiftName;//班次名称
    private Integer deptId;//部门id
    private String deptName;//部门名称
    private String type; //考勤类型 1 正常  2 迟到 3 早退 4 请假
    private String place;//打卡地点
    private String attendTime;//打卡时间
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
    private Integer dr;//逻辑删除标志
}