package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("ts_dept")
public class Dept {
    @TableId
    private Integer id; //主键id
    private Integer deptId;//部门编号
    private String deptName;//部门名称
    private Integer statu;//状态
    private Integer dr;//逻辑删除标志
    private Date createTime;//创建时间
    private Date updateTime;//更新时间
}
