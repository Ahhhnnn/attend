package com.he.attend.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.StaffMapper;
import com.he.attend.dao.UserMapper;
import com.he.attend.model.Staff;
import com.he.attend.model.User;
import com.he.attend.service.StaffService;
import com.he.attend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Staff> loginByPsd(String phone, String password) {
        return baseMapper.loginByPsd(phone,password);
    }

    @Override
    public List<Staff> queryByDeptId(Integer deptId) {
        EntityWrapper<Staff> entityWrapper=new EntityWrapper<Staff>();
        entityWrapper.eq("dept_id",deptId);
        return baseMapper.selectList(entityWrapper);
    }

    @Override
    public List<Staff> queryByStaffIds(List<Integer> staffIds) {
        EntityWrapper<Staff> entityWrapper=new EntityWrapper<Staff>();
        entityWrapper.in("staff_id",staffIds);
        return baseMapper.selectList(entityWrapper);
    }

    @Override
    public Integer queryStaffNum() {
        return baseMapper.queryStaffNum();
    }
}
