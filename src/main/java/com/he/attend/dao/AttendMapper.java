package com.he.attend.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.he.attend.model.Attend;
import com.he.attend.model.Staff;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttendMapper extends BaseMapper<Attend> {

    void logicDelete (Integer attendId);
}
