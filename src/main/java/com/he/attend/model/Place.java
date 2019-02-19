package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@TableName("ts_place")
public class Place {

    @TableId
    private Integer id;//主键id

    private String name;//名称

    private String relName;//地点真实名称

    private BigDecimal longitude;//经度

    private BigDecimal latitude;//纬度

    private Integer distance;//打卡有效距离

    private Integer statu;//状态

    private Integer dr;//逻辑删除标志

    private Date createTime;//创建时间

    private Date updateTime;//更新时间
}
