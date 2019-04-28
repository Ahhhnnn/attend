package com.he.attend.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.AttendMapper;
import com.he.attend.dao.PlaceMapper;
import com.he.attend.model.Attend;
import com.he.attend.model.Place;
import com.he.attend.service.AttendService;
import com.he.attend.service.PlaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaceServiceImpl extends ServiceImpl<PlaceMapper, Place> implements PlaceService {


    @Override
    public void updateById(Integer statu, Integer id) {
        baseMapper.updateById(statu,id);
    }

    @Override
    public void update(Place place) {
        baseMapper.update(place);
    }

    @Override
    public String queryPlaceNameById(Integer id) {
        return baseMapper.queryPlaceNameById(id);
    }

    @Override
    public Place queryById(Integer placeId) {
        EntityWrapper<Place> placeEntityWrapper=new EntityWrapper<>();
        placeEntityWrapper.eq("id",placeId);
        List<Place> placeList=baseMapper.selectList(placeEntityWrapper);
        return placeList.get(0);
    }
}
