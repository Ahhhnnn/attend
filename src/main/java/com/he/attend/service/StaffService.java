package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.Staff;
import com.he.attend.model.User;

public interface StaffService extends IService<Staff> {

    Staff getByStaffName(String staffName);

}
