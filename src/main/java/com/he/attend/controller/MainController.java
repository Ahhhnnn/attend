package com.he.attend.controller;

import com.he.attend.common.BaseController;
import com.he.attend.common.JsonResult;
import com.he.attend.model.User;
import com.he.attend.service.AuthoritiesService;
import com.he.attend.service.RoleService;
import com.he.attend.service.UserRoleService;
import com.he.attend.service.UserService;
import com.wangfan.endecrypt.utils.EndecryptUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.wf.jwtp.provider.Token;
import org.wf.jwtp.provider.TokenStore;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value = "个人相关接口", tags = "main")
@Controller
public class MainController extends BaseController {
    @Autowired
    private UserService userService;

   // private TokenStore tokenStore;
    @Autowired
    private AuthoritiesService authoritiesService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    @ApiIgnore
    @RequestMapping({"/", "/index"})
    public String index() {
        return "redirect:index.html";
    }

    @ResponseBody
    @ApiOperation(value = "获取个人信息")
    @ApiImplicitParam(name = "access_token", value = "令牌", required = true, dataType = "String", paramType = "query")
    @GetMapping("${api.version}/userInfo")
    public JsonResult userInfo(HttpServletRequest request) {
        return JsonResult.ok().put("user", userService.selectById(getLoginUserId(request)));
    }

    @ResponseBody
    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form")
    })
    @PostMapping("${api.version}/login")
    public JsonResult login(String username, String password, HttpServletRequest request) {
        User user = userService.getByUsername(username);
        if (user == null) {
            return JsonResult.error("账号不存在");
        } else if (!user.getPassword().equals(EndecryptUtils.encrytMd5(password))) {
            return JsonResult.error("密码错误");
        }
        String[] roles = arrayToString(userRoleService.getRoleIds(user.getUserId()));
        String[] permissions = listToArray(authoritiesService.listByUserId(user.getUserId()));
        //Token token = tokenStore.createNewToken(String.valueOf(user.getUserId()), permissions, roles);
        return JsonResult.ok("登录成功").put("access_token", "123456");
    }

    private String[] listToArray(List<String> list) {
        String[] strs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strs[i] = list.get(i);
        }
        return strs;
    }

    private String[] arrayToString(Object[] objs) {
        String[] strs = new String[objs.length];
        for (int i = 0; i < objs.length; i++) {
            strs[i] = String.valueOf(objs[i]);
        }
        return strs;
    }

}
