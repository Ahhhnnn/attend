package com.he.attend.controller.AppCon;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.he.attend.common.PageResult;
import com.he.attend.model.Dept;
import com.he.attend.model.Staff;
import com.he.attend.service.DeptService;
import com.he.attend.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author he
 * @title: AppStaffController
 * @projectName attendhn
 * @description: TODO
 * @date 2019/5/518:31
 */
@RestController
@RequestMapping("/appStaff")
public class AppStaffController {

    @Autowired
    private StaffService staffService;

    @Autowired
    private DeptService deptService;
    @RequestMapping("insert")
    public PageResult insertStaff(Staff staff){

        String deptName=staff.getDeptName();
        Dept dept=deptService.queryByDeptName(deptName);
        staff.setDeptId(dept.getDeptId());
        if(staffService.insert(staff)) {
            return new PageResult("注册成功", 200);
        }
        else {
            return new PageResult("注册失败", 400);
        }
    }
    /**
     * 查询所有状态正常且未删除的部门
     * @return
     */
    @RequestMapping("/queryAllStatuDepts")
    public PageResult<Dept> queryAllStatu(){
        EntityWrapper entity=new EntityWrapper();
        entity.eq("dr",0);
        entity.eq("statu",0);
        List<Dept> depts=deptService.selectList(entity);
        return new PageResult<>(200,"查询成功",depts.size(),depts);
    }

}
