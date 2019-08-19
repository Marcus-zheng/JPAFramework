package com.marcus.security;

import com.marcus.base.bean.SecuritySubject;
import org.springframework.stereotype.Component;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/19 17:20
 **/
@Component
public class SecurityServiceImpl implements SecurityService {

    @Override
    public SecuritySubject getLoginSubject(String sessionId) {
        return null;
    }
}
