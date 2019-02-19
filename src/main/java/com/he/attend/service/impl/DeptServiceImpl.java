package com.he.attend.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.AttendMapper;
import com.he.attend.dao.DeptMapper;
import com.he.attend.model.Attend;
import com.he.attend.model.Dept;
import com.he.attend.service.AttendService;
import com.he.attend.service.DeptService;
import org.springframework.stereotype.Service;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {


    @Override
    public void logicDelete(Integer deptId) {
        baseMapper.logicDelete(deptId);
    }

    @Override
    public void updateStatu(Integer deptId, Integer statu) {
        baseMapper.updateStatu(deptId,statu);
    }
}
