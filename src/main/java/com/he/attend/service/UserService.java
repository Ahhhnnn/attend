package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.User;

public interface UserService extends IService<User> {

    User getByUsername(String username);

}
