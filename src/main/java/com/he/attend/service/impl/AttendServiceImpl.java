package com.he.attend.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.AttendMapper;
import com.he.attend.dao.StaffMapper;
import com.he.attend.model.Attend;
import com.he.attend.model.Staff;
import com.he.attend.service.AttendService;
import com.he.attend.service.StaffService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendServiceImpl extends ServiceImpl<AttendMapper, Attend> implements AttendService {


    @Override
    public void logicDelete(Integer attendId) {
        baseMapper.logicDelete(attendId);
    }

    @Override
    public List<Attend> queryByStaffIdAndAttendDay(Integer staffId, String attendDay) {
        EntityWrapper<Attend> entityWrapper=new EntityWrapper<Attend>();
        entityWrapper.eq("staff_id",staffId);
        entityWrapper.like("attend_time",attendDay);
        entityWrapper.orderBy("attend_time",false);
        return baseMapper.selectList(entityWrapper);
    }
}
