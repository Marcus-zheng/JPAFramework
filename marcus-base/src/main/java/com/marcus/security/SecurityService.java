package com.marcus.security;

import com.marcus.base.bean.SecuritySubject;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/23 9:33
 **/
public interface SecurityService {

    SecuritySubject getLoginSubject(String sessionId);
}
