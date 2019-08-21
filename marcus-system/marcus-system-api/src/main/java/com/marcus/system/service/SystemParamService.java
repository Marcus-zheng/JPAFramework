package com.marcus.system.service;

import com.marcus.system.vo.SystemParamItem;

import java.util.Map;

public interface SystemParamService {

    /**
     * @Description: 保存实体
     * @author: Marcus.zheng
     * @date:  2019/7/22 7:09
     **/
    SystemParamItem save(SystemParamItem item);

    /**
     * @Description: 根据名称保存已有的参数值
     * @author: Marcus.zheng
     * @date:  2019/7/22 7:09
     * @param: [paramName, paramValue]
     * @return: void
     **/
    void saveValueByName(String paramName, String paramValue);

    /**
     * @Description: 初始化数据
     * @author: Marcus.zheng
     * @date:  2019/7/22 7:09
     **/
    void initData(SystemParamItem item);

    /**
     * @Description 获取参数
     * @Author Marcus.zheng
     * @Date 2019/7/22 13:58
     * @Param module
     * @Return java.util.Map<java.lang.String,java.lang.String>
     */
    Map<String, String> getParamsByModule(String module);

    /**
     * @Description: 根据参数名获取参数值
     * @author: Marcus.zheng
     * @date:  2019/7/22 7:10
     * @param: [paramName]
     * @return: java.lang.String
     **/
    String getValueByName(String paramName);

    /**
     * @Description: 保存参数
     * @author: Marcus.zheng
     * @date:  2019/7/22 7:10
     * @param: [params]
     * @return: void
     **/
    void saveParams(Map<String, String> params);

    /**
     * @Description: 设置模块已经初始化标记
     * @author: Marcus.zheng
     * @date:  2019/7/22 7:10
     * @param: [paramName]
     * @return: void
     **/
    void setAlreadyInitModule(String paramName);

    /**
     * @Description: 获取模块是否已经初始化
     * @author: Marcus.zheng
     * @date:  2019/7/22 7:11
     * @param: [paramName]
     * @return: boolean
     **/
    boolean getAlreadyInitModule(String paramName);
}
