package com.he.attend.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.he.attend.dao.AttendMapper;
import com.he.attend.dao.DeptMapper;
import com.he.attend.model.Attend;
import com.he.attend.model.Dept;
import com.he.attend.service.AttendService;
import com.he.attend.service.DeptService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    @Override
    public Dept queryByDeptName(String deptName) {
        EntityWrapper<Dept> deptEntityWrapper=new EntityWrapper<Dept>();
        deptEntityWrapper.eq("dept_name",deptName);
        List<Dept> deptList=baseMapper.selectList(deptEntityWrapper);
        if(CollectionUtils.isEmpty(deptList)){
            return null;
        }
        return deptList.get(0);
    }

    @Override
    public Dept queryByDeptId(Integer deptId) {
        EntityWrapper<Dept> deptEntityWrapper=new EntityWrapper<>();
        deptEntityWrapper.eq("dept_id",deptId);
        List<Dept> deptList=baseMapper.selectList(deptEntityWrapper);
        if(CollectionUtils.isEmpty(deptList)){
            return null;
        }
        return deptList.get(0);
    }
}
