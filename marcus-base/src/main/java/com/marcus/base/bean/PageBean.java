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
public class PageBean implements Serializable {
    private static final long serialVersionUID = -7953892880033122091L;

    // 第几页，从0开始算
    private int page = 0;
    // 每页几条
    private int size = 10;
    // 总条数
    private long total = 0;
    // 数据
    private Object rows;

}
