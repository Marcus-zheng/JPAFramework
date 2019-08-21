package com.marcus.system.service.impl;

import com.marcus.base.constants.BaseConstant;
import com.marcus.core.utils.ModelUtil;
import com.marcus.system.dao.SystemParamDao;
import com.marcus.system.model.SystemParam;
import com.marcus.system.service.SystemParamService;
import com.marcus.system.vo.SystemParamItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/21 18:03
 **/
public class SystemParamServiceImpl implements SystemParamService {
    @Autowired
    private SystemParamDao systemParamDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public SystemParamItem save(SystemParamItem item) {
        SystemParam systemParam = Optional.ofNullable(item).filter(i -> Objects.nonNull(i.getId()))
                .flatMap(p -> systemParamDao.findById(p.getId())).orElse(new SystemParam());
        ModelUtil.copyProperties(item, systemParam);
        systemParamDao.save(systemParam);
        stringRedisTemplate.opsForValue().set(BaseConstant.SYSTEM_PARAM + item.getParamName(), item.getParamValue());
        item.setId(systemParam.getId());
        return item;
    }

    @Override
    public void saveValueByName(String paramName, String paramValue) {
        SystemParam systemParam = systemParamDao.findByParamName(paramName);
        if (Objects.isNull(systemParam)){
            // 新增
            systemParam = new SystemParam();
            systemParam.setParamName(paramName);
        }
        systemParam.setParamValue(paramValue);
        systemParamDao.save(systemParam);
        stringRedisTemplate.opsForValue().set("system:param:" + paramName, paramValue);
    }

    @Override
    public void initData(SystemParamItem item) {
        SystemParam systemParam = systemParamDao.findByParamName(item.getParamName());
        if (Objects.isNull(systemParam)) {
            save(item);
        }
    }

    @Override
    public Map<String, String> getParamsByModule(String module) {
        List<SystemParam> systemParamList = systemParamDao.findByParamNameLike(module);
        Map<String, String> map = new HashMap<>();
        systemParamList.forEach(systemParam -> {
            map.put(systemParam.getParamName(), systemParam.getParamValue());
        });
        return map;
    }

    @Override
    public String getValueByName(String paramName) {
        String value = stringRedisTemplate.opsForValue().get(BaseConstant.SYSTEM_PARAM + paramName);
        if (StringUtils.isBlank(value)){
            SystemParam systemParam = systemParamDao.findByParamName(paramName);
            if (Objects.nonNull(systemParam)){
                value = systemParam.getParamValue();
                stringRedisTemplate.opsForValue().set("system:param:" + paramName, value);
            }
        }
        return value;
    }

    @Override
    public void saveParams(Map<String, String> params) {
        params.forEach((name,value) -> {
            saveValueByName(name, value);
        });
    }

    @Override
    public void setAlreadyInitModule(String paramName) {
        SystemParam systemParam = systemParamDao.findByParamName(paramName);
        if (Objects.isNull(systemParam)) {
            // 新增
            systemParam = new SystemParam();
        }
        systemParam.setParamName(paramName);
        systemParam.setParamValue("true");
        systemParam.setDescription(paramName+" Module Init Flag");
        systemParamDao.save(systemParam);
    }

    @Override
    public boolean getAlreadyInitModule(String paramName) {
        SystemParam systemParam = systemParamDao.findByParamName(paramName);
        return Objects.nonNull(systemParam) && "true".equals(systemParam.getParamValue());
    }
}
