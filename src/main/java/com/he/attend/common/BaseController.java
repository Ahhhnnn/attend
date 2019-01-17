package com.he.attend.common;


import javax.servlet.http.HttpServletRequest;
import org.wf.jwtp.provider.Token;
import org.wf.jwtp.util.SubjectUtil;
/**
 * Controller基类
 * Created by wangfan on 2018-02-22 上午 11:29.
 */
public class BaseController {

    /**
     * 获取当前登录的userId
     */
    public Integer getLoginUserId(HttpServletRequest request) {
        Token token = SubjectUtil.getToken(request);
        System.out.println("----------------------");
        System.out.println(token);
        System.out.println("----------------------");
        return token == null ? null : Integer.parseInt(token.getUserId());
    }

}
