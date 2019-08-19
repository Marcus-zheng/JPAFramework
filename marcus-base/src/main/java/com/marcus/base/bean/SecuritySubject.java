package com.marcus.base.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/19 14:09
 **/
@Setter
@Getter
@Accessors(chain = true)
public class SecuritySubject implements Serializable {
    private static final long serialVersionUID = -2993150305245945362L;

    // 用户id
    private Long userId;
    // 用户登录账号
    private String loginName;
    // 用户姓名
    private String userName;
    // 是否超级管理员
    private Boolean isSuperuser;
}
