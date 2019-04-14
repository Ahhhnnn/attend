package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.Attend;
import com.he.attend.model.Place;

public interface PlaceService extends IService<Place> {
    void updateById (Integer statu,Integer id);
    void update(Place place);
    String queryPlaceNameById(Integer id);
}
