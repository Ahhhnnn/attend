package com.he.attend.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.he.attend.model.Staff;
import com.he.attend.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StaffMapper extends BaseMapper<Staff> {

    Staff getByStaffName(String username);

    Staff queryByStaffId(@Param("staffId") Integer staffId);

    List<Staff> loginByPsd(@Param("phone") String phone,@Param("password") String password);
}
