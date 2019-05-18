package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
@TableName("ts_rule")
public class Rule {
    @Id
    private Integer ruleId;

    private String ruleName;

    private String deptIds;//适用部门ids

    private String staffIds;

    private Integer shiftId;

    private String shiftName;

    private String days; //排班日期  以逗号分隔

    private Integer placeId; //考勤地点id 外键

    private String placeName;//考勤地点名称

    private Integer isWeekendAttend;//周末是否打卡 1：打卡 0 ：不打卡

    private Integer isHolidayAttend;//公休日是否打卡 1：打卡 0 ：不打卡

    private Date createTime;  // 创建时间

    private Date updateTime;  // 修改时间
    @TableField(exist = false)
    private List<Dept> depts; //适用的部门
    @TableField(exist = false)
    private List<Staff> staffs;//适用的人员


}
