package com.marcus.base.controller;

import com.marcus.base.bean.SecuritySubject;
import com.marcus.base.constants.BaseConstant;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/19 14:45
 **/
public class BaseController {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    protected SecuritySubject getLoginSubject(){
        SecuritySubject securitySubject = (SecuritySubject) request.getSession().getAttribute(BaseConstant.SESSION_USER);
        return securitySubject;
    }
}
