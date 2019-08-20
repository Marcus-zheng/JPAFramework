package com.marcus.auth.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName AuthRoleItem
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/14 11:43
 * @Version 1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class AuthRoleItem implements Serializable {
    private static final long serialVersionUID = 1L;

    // 主键id
    private Long id;
    // 角色编号
    private String code;
    // 角色名称
    private String name;
    // 备注
    private String remark;
    // 用户关联角色id;
    private String permissionIds;

    public AuthRoleItem(){
        super();
    }

    public AuthRoleItem(String code, String name, String remark){
        super();
        this.code = code;
        this.name = name;
        this.remark = remark;
    }
}
