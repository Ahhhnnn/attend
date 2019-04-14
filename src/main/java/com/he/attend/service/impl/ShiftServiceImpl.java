package com.he.attend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.ShiftMapper;
import com.he.attend.model.Shift;
import com.he.attend.service.ShiftService;
import org.springframework.stereotype.Service;

@Service
public class ShiftServiceImpl extends ServiceImpl<ShiftMapper,Shift> implements ShiftService {
    @Override
    public Shift getByShiftName(String shiftName) {
        return baseMapper.getByShiftName(shiftName);
    }

    @Override
    public void updateById(Integer state,Integer shiftId) {
        baseMapper.updateById(state,shiftId);
    }

    @Override
    public void deleteById(Integer shiftId) {
        baseMapper.deleteById(shiftId);
    }

    @Override
    public void update(Shift shift) {
        baseMapper.update(shift);
    }

    @Override
    public Shift getShiftById(Integer shiftId) {
        return baseMapper.getShiftById(shiftId);
    }
}
