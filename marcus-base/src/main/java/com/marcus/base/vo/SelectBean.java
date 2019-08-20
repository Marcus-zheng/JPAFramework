package com.marcus.base.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName SelectBean
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/7/14 9:44
 * @Version 1.0
 **/
@Setter
@Getter
@Accessors(chain = true)
public class SelectBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object text;
    private Object value;
    private boolean selected;
}
