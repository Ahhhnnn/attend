package com.he.attend.common.utils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.he.attend.common.PageResult;
import com.he.attend.model.Role;
import com.he.attend.model.RoleAuthorities;
import com.he.attend.model.UserRole;
import com.he.attend.service.RoleAuthoritiesService;
import com.he.attend.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wf.jwtp.provider.Token;
import org.wf.jwtp.util.SubjectUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author he
 * @title: PowerUtil
 * @projectName attendhn
 * @description: TODO
 * @date 2019/5/817:41
 */
public class PowerUtil {


    /**
     * 用戶接口權限认证
     * @param authority
     * @param request
     * @return
     */
    public static  boolean powerPermisson(String authority, HttpServletRequest request){
        Token token= SubjectUtil.getToken(request);
        String[] permissons=token.getPermissions();
        List<String> authoritys= Arrays.asList(permissons);
        if(!authoritys.contains(authority)){
            return false;
        }
        return true;
    }
}
