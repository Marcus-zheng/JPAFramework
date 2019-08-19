package com.marcus.security;

import com.marcus.base.bean.SecuritySubject;

public interface SecurityService {

    SecuritySubject getLoginSubject(String sessionId);
}
