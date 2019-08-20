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
 * @Date 2019/8/20 10:56
 **/
@Entity
@Table(name="auth_role")
@Getter
@Setter
@Accessors(chain=true)
public class AuthRole extends BaseModel implements Serializable {
    private static final long serialVersionUID = -3419412853835803447L;

    // 角色编号
    @Column(name="code",length=50, nullable=false, unique=true)
    private String code;
    // 角色名称
    @Column(name="name",length=30, nullable=false)
    private String name;
    // 备注
    @Column(name="remark",length=50)
    private String remark;

    @ManyToMany(targetEntity = AuthUser.class)
    @JoinTable(name="auth_user_role",
            joinColumns=@JoinColumn(name="auth_role_id",referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="auth_user_id",referencedColumnName="id"))
    private List<AuthUser> authUsers = new ArrayList<>();

    @ManyToMany(targetEntity = AuthPermission.class,fetch = FetchType.LAZY)
    @JoinTable(name="auth_role_permission",
            joinColumns=@JoinColumn(name="auth_role_id",referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="auth_permission_id",referencedColumnName="id"))
    private List<AuthPermission> authPermissions = new ArrayList<>();

    public AuthRole(){
        super();
    }

    public AuthRole(String code, String name, String remark){
        super();
        this.code = code;
        this.name = name;
        this.remark = remark;
    }
}
