package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author duhan
 * @title: DayReport
 * @projectName attendhn
 * @description: TODO
 * @date 2019/4/2120:31
 */
@Data
@TableName("ts_day_report")
public class DayReport
{
    @TableId
    private Integer id;

    private String day;

    private Integer staffId;

    private String staffName;

    private String firAttendTime;

    private String secAttendTime;

    private String workHour;

    private String notWorkHour;

    private Date createTime;  // 创建时间

    private Date updateTime;  // 修改时间
}
