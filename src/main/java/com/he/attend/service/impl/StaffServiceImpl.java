package com.he.attend.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.StaffMapper;
import com.he.attend.dao.UserMapper;
import com.he.attend.model.Staff;
import com.he.attend.model.User;
import com.he.attend.service.StaffService;
import com.he.attend.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {


    @Override
    public Staff getByStaffName(String staffName) {
        return baseMapper.getByStaffName(staffName);
    }

    @Override
    public Staff queryByStaffId(Integer staffId) {
        return baseMapper.queryByStaffId(staffId);
    }
}
