package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.UserRole;

/**
 * Created by Administrator on 2018-12-19 下午 4:09.
 */
public interface UserRoleService extends IService<UserRole> {

    Integer[] getRoleIds(Integer userId);
}
