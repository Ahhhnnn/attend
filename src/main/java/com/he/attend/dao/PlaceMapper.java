package com.he.attend.dao;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.he.attend.model.Attend;
import com.he.attend.model.Place;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlaceMapper extends BaseMapper<Place> {
    void updateById(@Param("statu") Integer statu, @Param("id") Integer id);
    void update(Place place);
}
