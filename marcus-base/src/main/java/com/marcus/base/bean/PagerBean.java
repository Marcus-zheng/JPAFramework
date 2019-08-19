package com.marcus.base.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/19 14:01
 **/
@Setter
@Getter
@Accessors(chain = true)
public class PagerBean implements Serializable {
    private static final long serialVersionUID = -7953892880033122091L;

    // 每页几条
    private int limit = 10;
    // 第几页，从0开始算
    private int offset = 0;
    // 总条数
    private long total = 0;
    // 数据
    private Object rows;

}
