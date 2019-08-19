package com.marcus.security;

import com.alibaba.fastjson.JSON;
import com.marcus.base.bean.SecuritySubject;
import com.marcus.base.constants.BaseConstant;
import com.marcus.base.vo.ResultMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/19 17:20
 **/
@ServletComponentScan
@Component
@WebFilter(filterName = "securityFilter", urlPatterns = {"/*"})
public class SecurityFilter implements Filter {

    @Autowired
    private SecurityService securityService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (matchUrl(request)) {
            SecuritySubject securitySubject = (SecuritySubject) request.getSession().getAttribute(BaseConstant.SESSION_USER);
//            logger.info("request [" + request.getRequestURI() + "] session id is " + request.getSession().getId());
            if (Objects.isNull(securitySubject)) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
//                String loginName = CookieHelper.getCookieValue(request, BaseConstant.COOKIE_USER);
//                if (StringUtils.isBlank(loginName)) {
//                    logger.info("not exists user");
//                    if (isAjaxRequest(request)) {
//                        response.setContentType("application/json;charset=UTF-8");
//                        PrintWriter writer = response.getWriter();
//                        writer.write(JSON.toJSONString(ResultMessage.returnFail(-500, "session过期")));
//                        writer.flush();
//                        writer.close();
//                    } else {
//                        response.sendRedirect("/login");
//                    }
//                    return;
//                }
//                AuthUserVo authUserVo = authUserService.getByLoginName(loginName);
//                securitySubject = new SecuritySubject();
//                securitySubject.setUserId(authUserVo.getId())
//                        .setLoginName(authUserVo.getLoginName())
//                        .setName(authUserVo.getName())
//                        .setIsSuperuser(authUserVo.getIsSuperuser());
//                request.getSession().setAttribute(BaseConstant.SESSION_USER, securitySubject);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);

    }


    private boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        return header != null && header.contains("XMLHttpRequest");
    }


    private boolean matchUrl(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return !(requestURI.equals("/")
                || requestURI.startsWith("/login")
                || requestURI.startsWith("/doLogin")
                || requestURI.startsWith("/doLogout")
                || requestURI.startsWith("/static")
                || requestURI.startsWith("/error")
                || requestURI.startsWith("/api/"));
    }
}
