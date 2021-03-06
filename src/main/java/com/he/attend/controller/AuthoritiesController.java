package com.he.attend.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.he.attend.common.BaseController;
import com.he.attend.common.JsonResult;
import com.he.attend.common.PageResult;
import com.he.attend.common.utils.PowerUtil;
import com.he.attend.common.utils.ReflectUtil;
import com.he.attend.model.Authorities;
import com.he.attend.model.RoleAuthorities;
import com.he.attend.service.AuthoritiesService;
import com.he.attend.service.RoleAuthoritiesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(value = "权限管理相关的接口", tags = "authorities")
@RestController
@RequestMapping("${api.version}/authorities")
public class AuthoritiesController extends BaseController {
    @Autowired
    private AuthoritiesService authoritiesService;
    @Autowired
    private RoleAuthoritiesService roleAuthoritiesService;

    @ApiOperation(value = "同步权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "json", value = "权限列表json", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/sync")
    public JsonResult add(String json) {
        try {
            List<Authorities> list = new ArrayList<>();
            JSONObject jsonObject = JSON.parseObject(json);
            JSONObject paths = jsonObject.getJSONObject("paths");
            Set<String> pathsKeys = paths.keySet();
            for (String pathKey : pathsKeys) {
                JSONObject apiObject = paths.getJSONObject(pathKey);
                Set<String> apiKeys = apiObject.keySet();
                for (String apiKey : apiKeys) {
                    JSONObject methodObject = apiObject.getJSONObject(apiKey);
                    Authorities authorities = new Authorities();
                    authorities.setAuthority(apiKey + ":" + pathKey);
                    authorities.setAuthorityName(methodObject.getString("summary"));
                    list.add(authorities);
                }
            }
            authoritiesService.delete(null);
            authoritiesService.insertBatch(list);
            roleAuthoritiesService.deleteTrash();
            return JsonResult.ok("同步成功");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.error("同步失败");
        }
    }

    @ApiOperation(value = "查询所有权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping
    public PageResult<Map<String, Object>> list(Integer roleId,HttpServletRequest request) {
        /*String authority="delete:/authorities/role";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return  JsonResult.error("没有相应权限");
        }*/
        List<Map<String, Object>> maps = new ArrayList<>();
        List<Authorities> authorities = authoritiesService.selectList(null);
        List<String> roleAuths = authoritiesService.listByRoleId(roleId);
        for (Authorities one : authorities) {
            Map<String, Object> map = ReflectUtil.objectToMap(one);
            map.put("checked", 0);
            for (String roleAuth : roleAuths) {
                if (one.getAuthority().equals(roleAuth)) {
                    map.put("checked", 1);
                    break;
                }
            }
            maps.add(map);
        }
        return new PageResult<>(maps);
    }

    @ApiOperation(value = "给角色添加权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "authId", value = "权限id", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("/role")
    public JsonResult addRoleAuth(Integer roleId, String authId,HttpServletRequest request) {
        String authority="post:/authorities/role";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return  JsonResult.error("没有对应的权限");
        }
        RoleAuthorities roleAuth = new RoleAuthorities();
        roleAuth.setRoleId(roleId);
        roleAuth.setAuthority(authId);
        if (roleAuthoritiesService.insert(roleAuth)) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }

    @ApiOperation(value = "移除角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "authId", value = "权限id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "query")
    })
    @DeleteMapping("/role")
    public JsonResult deleteRoleAuth(String roleId, String authId, HttpServletRequest request) {
        String authority="delete:/authorities/role";
        boolean canDo = PowerUtil.powerPermisson(authority,request);
        if(!canDo){
            return  JsonResult.error("没有相应权限");
        }
        if (roleAuthoritiesService.delete(new EntityWrapper<RoleAuthorities>().eq("role_id", roleId).eq("authority", authId))) {
            return JsonResult.ok();
        }
        return JsonResult.error();
    }
}
