package com.he.attend.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.he.attend.model.Attend;
import com.he.attend.model.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

    void logicDelete(Integer deptId);
    void updateStatu(@Param("deptId") Integer deptId, @Param("statu") Integer statu);
}
