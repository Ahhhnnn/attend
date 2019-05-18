package com.he.attend.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.he.attend.common.JsonResult;
import com.he.attend.common.PageResult;
import com.he.attend.common.utils.PowerUtil;
import com.he.attend.model.Role;
import com.he.attend.model.RoleAuthorities;
import com.he.attend.model.UserRole;
import com.he.attend.service.RoleAuthoritiesService;
import com.he.attend.service.RoleService;
import com.he.attend.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wf.jwtp.provider.Token;
import org.wf.jwtp.util.SubjectUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "角色相关的接口", tags = "role")
@RestController
@RequestMapping("${api.version}/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleAuthoritiesService roleAuthoritiesService;
    @ApiOperation(value = "查询所有角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "搜索关键字", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping()
    public PageResult<Role> list(String keyword,HttpServletRequest request) {
        List<Role> list = roleService.selectList(new EntityWrapper<Role>().orderBy("create_time", true));
        // 筛选结果
        if (keyword != null && !keyword.trim().isEmpty()) {
            keyword = keyword.trim();
            Iterator<Role> iterator = list.iterator();
            while (iterator.hasNext()) {
                Role next = iterator.next();
                boolean b = next.getRoleName().contains(keyword) || next.getComments().contains(keyword);
                if (!b) {
                    iterator.remove();
                }
            }
        }
        return new PageResult<>(list);
    }

    @ApiOperation(value = "添加角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role", value = "角色信息", required = true, dataType = "Role", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping()
    public JsonResult add(Role role,HttpServletRequest request) {
        String authority="post:/role";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return  JsonResult.error("没有相应权限");
        }
        if (roleService.insert(role)) {
            return JsonResult.ok("添加成功");
        }
        return JsonResult.error("添加失败");
    }

    @ApiOperation(value = "修改角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "role", value = "角色信息", required = true, dataType = "Role", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @PutMapping()
    public JsonResult update(Role role,HttpServletRequest request) {
        String authority="put:/role";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return  JsonResult.error("没有相应权限");
        }
        if (roleService.updateById(role)) {
            return JsonResult.ok("修改成功！");
        }
        return JsonResult.error("修改失败！");
    }

    @ApiOperation(value = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "query")
    })
    @DeleteMapping("/{id}")
    public JsonResult delete(@PathVariable("id") Integer roleId, HttpServletRequest request) {
        String authority="delete:/role";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return  JsonResult.error("没有相应权限");
        }
        if (roleService.deleteById(roleId)) {
            return JsonResult.ok("删除成功");
        }
        return JsonResult.error("删除失败");
    }


}
