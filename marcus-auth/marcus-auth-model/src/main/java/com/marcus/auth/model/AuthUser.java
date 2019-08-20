package com.marcus.auth.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/20 10:40
 **/
@Entity
@Table(name="auth_user")
@Getter
@Setter
@Accessors(chain=true)
public class AuthUser extends BaseModel implements Serializable {
    private static final long serialVersionUID = 6919028501876630687L;

    // 登录账号
    @Column(name="login_name", length=30, unique=true)
    private String loginName;
    // 登录密码
    @Column(name="login_pwd",length=128)
    private String loginPwd;
    // 用户姓名
    @Column(name="name",length=30)
    private String name;
    // 用户手机号码
    @Column(name="phone",length=50)
    private String phone;
    // 用户邮箱
    @Column(name="email",length=100)
    private String email;
    // 是否是超级用户
    @Column(name="is_superuser")
    private Boolean isSuperuser;
    // 初始化标识
    @Column(name="init_flag")
    private Boolean initFlag;

    @ManyToMany(targetEntity = AuthRole.class,fetch = FetchType.LAZY)
    @JoinTable(name="auth_user_role",
            joinColumns=@JoinColumn(name="auth_user_id",referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="auth_role_id",referencedColumnName="id"))
    private List<AuthRole> authRoles = new ArrayList<>();
}
