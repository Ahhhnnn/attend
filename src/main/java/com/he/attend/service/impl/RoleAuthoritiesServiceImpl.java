package com.he.attend.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.RoleAuthoritiesMapper;
import com.he.attend.model.RoleAuthorities;
import com.he.attend.service.RoleAuthoritiesService;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018-12-19 下午 4:39.
 */
@Service
public class RoleAuthoritiesServiceImpl extends ServiceImpl<RoleAuthoritiesMapper, RoleAuthorities> implements RoleAuthoritiesService {

    @Override
    public void deleteTrash() {
        baseMapper.deleteTrash();
    }

}
