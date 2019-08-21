package com.marcus.system.dao;

import com.marcus.system.model.SystemParam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/21 18:03
 **/
public interface SystemParamDao extends JpaRepository<SystemParam, Long> {

    SystemParam findByParamName(String paramName);

    List<SystemParam> findByParamNameLike(String module);
}
