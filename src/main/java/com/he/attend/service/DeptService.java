package com.he.attend.service;


import com.baomidou.mybatisplus.service.IService;
import com.he.attend.model.Attend;
import com.he.attend.model.Dept;

public interface DeptService extends IService<Dept> {

    void logicDelete(Integer deptId);
    void updateStatu(Integer deptId,Integer statu);
}
