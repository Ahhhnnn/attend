package com.he.attend.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;
@Data
@TableName("ts_staff")
public class Staff {
    @TableId
    private Integer staffId;//员工id

    private String staffName;//员工姓名

    private String sex;//性别

    private String phone;//手机号

    private String password;//密码

    private String email;//邮箱

    private String birthday;//生日

    private Integer deptId;//部门id

    private String deptName;    //部门名称

    private Integer state;     //状态

    private Date createTime;  // 注册时间

    private Date updateTime;  // 修改时间

}
