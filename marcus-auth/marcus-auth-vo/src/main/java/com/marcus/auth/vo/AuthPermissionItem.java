package com.marcus.auth.vo;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName AuthPermissionItem
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/14 11:43
 * @Version 1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class AuthPermissionItem implements Serializable {
    private static final long serialVersionUID = 1L;

    // 主键id
    private Long id;
    // 权限编码
    private String code;
    // 权限名称
    private String name;
    // 功能权限安全编码
    private String permission;
    // 资源类型，分系统、菜单、按钮【'system','menu','button'】
    private String resourceType;
    // 资源路径
    private String resourceLink;
    // 排序编号，对菜单显示顺序进行控制
    private Integer orderNo;
    // 显示图标
    private String img;
    // 鼠标移上去变化图标
    private String imgHover;
    // 父权限id
    private Long parentId;

    public AuthPermissionItem(){
        super();
    }

    public AuthPermissionItem(String code, String name, String permission, String resourceType, Integer orderNo){
        super();
        this.code = code;
        this.name = name;
        this.permission = permission;
        this.resourceType = resourceType;
        this.orderNo = orderNo;
    }
}
