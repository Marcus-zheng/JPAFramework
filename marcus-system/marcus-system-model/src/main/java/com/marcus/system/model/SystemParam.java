package com.marcus.system.model;

import com.marcus.auth.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/21 18:23
 * @Version 1.0
 **/
@Entity
@Table(name = "base_sysparam")
@Getter
@Setter
@Accessors(chain=true)
public class SystemParam extends BaseModel implements Serializable {
    private static final long serialVersionUID = 8852047834812153944L;

    // 参数名称
    @Column(name = "PARAM_NAME", length = 50, unique = true)
    private String paramName;
    // 参数值
    @Column(name = "PARAM_VALUE")
    private String paramValue;
    // 描述
    @Column(name = "DESCRIPTION")
    private String description;
}
