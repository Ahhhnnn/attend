package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.RoleAuthorities;

/**
 * Created by Administrator on 2018-12-19 下午 4:38.
 */
public interface RoleAuthoritiesService extends IService<RoleAuthorities> {

    void deleteTrash();
}
