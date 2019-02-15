package com.he.attend.service;

import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.Shift;

public interface ShiftService extends IService<Shift>{

    Shift getByShiftName(String shiftName);
    void updateById(Integer state,Integer shiftId);
    void deleteById(Integer shiftId);
    void update(Shift shift);
}
