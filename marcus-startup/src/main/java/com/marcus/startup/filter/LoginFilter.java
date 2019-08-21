package com.marcus.startup.filter;

import com.alibaba.fastjson.JSON;
import com.marcus.auth.service.AuthUserService;
import com.marcus.auth.vo.AuthUserItem;
import com.marcus.base.bean.SecuritySubject;
import com.marcus.base.constants.BaseConstant;
import com.marcus.base.vo.ResultMessage;
import com.marcus.startup.utils.CookieHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;


/**
 * 登录过滤器，判断是否登录
 * @author Charles
 * @since 2019-07-11 15:33
 */
@Component
@WebFilter(filterName = "loginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {

    @Autowired
    private AuthUserService authUserService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (matchUrl(request))
        {
            SecuritySubject loginUser = (SecuritySubject) request.getSession().getAttribute(BaseConstant.SESSION_USER);
            log.info("request [" + request.getRequestURI() + "] session id is " + request.getSession().getId());
            if (Objects.isNull(loginUser))
            {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                String loginName = CookieHelper.getCookieValue(request, BaseConstant.COOKIE_USER);
                if (StringUtils.isBlank(loginName))
                {
                    log.info("not exists user");
                    if (isAjaxRequest(request))
                    {
                        response.setContentType("application/json;charset=UTF-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(JSON.toJSONString(ResultMessage.returnFail(-500,"session过期")));
                        writer.flush();
                        writer.close();
                    }
                    else
                    {
                        response.sendRedirect("/login");
                    }
                    return;
                }
                AuthUserItem authUserItem = authUserService.getByLoginName(loginName);
                loginUser = new SecuritySubject();
                loginUser.setUserId(authUserItem.getId())
                        .setLoginName(authUserItem.getLoginName())
                        .setUserName(authUserItem.getUserName())
                        .setIsSuperuser(authUserItem.getIsSuperuser());
                request.getSession().setAttribute(BaseConstant.SESSION_USER, loginUser);
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);

    }


    private boolean isAjaxRequest(HttpServletRequest request)
    {
        String header = request.getHeader("X-Requested-With");
        return header != null && header.contains("XMLHttpRequest");
    }


    private boolean matchUrl(HttpServletRequest request)
    {
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
