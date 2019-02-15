package com.he.attend.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.he.attend.model.Shift;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ShiftMapper extends BaseMapper<Shift> {

    Shift getByShiftName(String shiftName);
    void updateById(@Param("state") Integer state, @Param("shiftId") Integer shiftId);
    void deleteById(@Param("shiftId")Integer shiftId);
    void update(Shift shift);
}
