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
@Table(name="auth_permission")
@Getter
@Setter
@Accessors(chain=true)
public class AuthPermission extends BaseModel implements Serializable {
    private static final long serialVersionUID = -1371546549657756064L;

    // 权限编码
    @Column(name = "code", length=50, unique=true)
    private String code;
    // 权限名称
    @Column(name = "name",length=50)
    private String name;
    // 功能权限安全编码
    @Column(name = "permission",length=128)
    private String permission;
    // 资源类型，分系统、菜单、按钮【'system','menu','button'】
    @Column(name = "resource_type")
    private String resourceType;
    // 资源路径
    @Column(name = "resource_link")
    private String resourceLink;
    // 排序编号，对菜单显示顺序进行控制
    @Column(name = "order_no")
    private Integer orderNo;
    // 显示图标
    @Column(name = "img",length=120)
    private String img;
    // 鼠标移上去变化图标
    @Column(name = "img_hover",length=120)
    private String imgHover;
    /**关联表  多个权限 对 多个角色*/
    @ManyToMany(targetEntity = AuthRole.class)
    @JoinTable(name="auth_role_permission",
            joinColumns=@JoinColumn(name="auth_permission_id",referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="auth_role_id",referencedColumnName="id"))
    private List<AuthRole> authRoles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "auth_permission_parent_id")
    private AuthPermission parent;
    /**关联子权限，一般是系统>菜单>按钮，一级级下来*/
    @OneToMany(mappedBy = "parent")
    private List<AuthPermission> children = new ArrayList<>();
}
