package com.he.attend.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.he.attend.model.Authorities;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface AuthoritiesMapper extends BaseMapper<Authorities> {

    List<String> listByUserId(Integer userId);

    List<String> listByRoleId(@Param("roleIds") List<Integer> roleIds);
}
