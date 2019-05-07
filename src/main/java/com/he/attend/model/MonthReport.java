package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author duhan
 * @title: MonthReport
 * @projectName attendhn
 * @description: TODO
 * @date 2019/4/2120:32
 */
@Data
@TableName("ts_month_report")
public class MonthReport {

    @TableId
    private Integer id;

    private String month;

    private Integer staffId;

    private String staffName;

    private Integer deptId;

    private String deptName;

    private String shoudWorkHour;//应出勤工时

    private String workHour;

    private String notWorkHour;

    private Integer lateNum; //迟到次数

    private Integer leaveEarlyNum;//早退次数

    private Integer notWorkDay; //旷工天数

    private Date createTime;  // 创建时间

    private Date updateTime;  // 修改时间
}
