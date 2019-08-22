package com.marcus.startup.controller;

import com.marcus.auth.service.AuthUserService;
import com.marcus.auth.vo.AuthUserItem;
import com.marcus.base.bean.SecuritySubject;
import com.marcus.base.constants.BaseConstant;
import com.marcus.base.exception.BusinessException;
import com.marcus.base.vo.ResultMessage;
import com.marcus.startup.utils.CookieHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author Charles
 * @since 2019-07-08
 */
@Controller
public class LoginController{
    @Autowired
    private AuthUserService authUserService;

    @RequestMapping("/login")
    public String loginPage(){
        return "login/login";
    }


    @ResponseBody
    @PostMapping("/doLogin")
    public ResultMessage doLogin(HttpServletRequest request, HttpServletResponse response){
        String loginName = request.getParameter("loginName");
        String loginPwd = request.getParameter("loginPwd");
        if (StringUtils.isBlank(loginName)){
            throw new BusinessException(ResultMessage.FAIL, "用户名不能为空");
        }
        if (StringUtils.isBlank(loginPwd)) {
            throw new BusinessException(ResultMessage.FAIL, "密码不能为空");
        }
        AuthUserItem authUserVo = authUserService.getByLoginName(loginName);
        if (authUserVo == null) {
            throw new BusinessException(ResultMessage.FAIL, "用户名或密码错误");
        }
        //前端传进来都是md5加密，无需再次加密修改
        if (!loginPwd.equals(authUserVo.getLoginPwd())) {
            throw new BusinessException(ResultMessage.FAIL, "用户名或密码错误");
        }
        SecuritySubject loginUser = new SecuritySubject();
        loginUser.setUserId(authUserVo.getId())
                .setLoginName(authUserVo.getLoginName())
                .setUserName(authUserVo.getUserName())
                .setIsSuperuser(authUserVo.getIsSuperuser());
        // TODO 考虑要不要将用户菜单权限放到session里，暂时没放
        request.getSession().setAttribute(BaseConstant.SESSION_USER, loginUser);
        CookieHelper.setCookie(response,BaseConstant.COOKIE_USER, loginUser.getLoginName(), (int) TimeUnit.MINUTES.toSeconds(30L));
        return ResultMessage.resultSuccess("");
    }


    @ResponseBody
    @PostMapping("/doForget")
    public ResultMessage doForget(HttpServletRequest request, HttpServletResponse response){
        return ResultMessage.resultSuccess();
    }

    @ResponseBody
    @PostMapping("/doLogout")
    public ResultMessage doLogout(HttpServletRequest request, HttpServletResponse response){
        //清除cookie 、session
//        request.getSession().removeAttribute(SystemConstants.SESSION_STORE_USER);
        request.getSession().invalidate();
        CookieHelper.clearCookie(request,response,BaseConstant.COOKIE_USER);
        return ResultMessage.resultSuccess();
    }
}
