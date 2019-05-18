package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.Staff;
import com.he.attend.model.User;

import java.util.List;

public interface StaffService extends IService<Staff> {

    Staff getByStaffName(String staffName);

    Staff queryByStaffId(Integer staffId);

    List<Staff> loginByPsd(String phone, String password);

    List<Staff> queryByDeptId(Integer deptId);

    List<Staff> queryByStaffIds(List<Integer> staffIds);

    Integer queryStaffNum();
}
