package com.marcus.security;

import com.marcus.base.bean.SecuritySubject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/23 9:41
 **/
@Component
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    private SessionManager sessionManager;

    @Override
    public SecuritySubject getLoginSubject(String sessionId) {
        return sessionManager.getLoginSubject(sessionId);
    }
}
