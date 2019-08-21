package com.marcus.core.utils;

import com.marcus.base.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * @ClassName ModelUtil
 * @Description 对象拷贝工具
 * @Author Marcus.zheng
 * @Date 2019/7/14 12:05
 * @Version 1.0
 **/
public class ModelUtil {
    public static <T> T copyProperties(Object source, T target) {
        try {
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception var3) {
            throw new BusinessException(BusinessException.EXCEPTIONLEVEL_ERROR, "bean copy error");
        }
    }

    public static <T> T copyPropertiesWithIgnore(Object source, T target, String... ignoreProperties) {
        try {
            BeanUtils.copyProperties(source, target, ignoreProperties);
            return target;
        } catch (Exception var4) {
            throw new BusinessException(BusinessException.EXCEPTIONLEVEL_ERROR, "bean ignoreProperties error");
        }
    }

    public static <T> List<T> copyListProperties(Collection list, Class<T> classs) {
        try {
            List<T> targetList = new ArrayList<>();

            for (Object source : list) {
                targetList.add(copyProperties(source, classs.newInstance()));
            }
            return targetList;
        } catch (Exception var5) {
            throw new BusinessException(BusinessException.EXCEPTIONLEVEL_ERROR, "bean error");
        }
    }

    public static <T> List<T> copyListPropertiesWithIgnore(Collection list, Class<T> classs, String... ignoreProperties) {
        try {
            List<T> targetList = new ArrayList<>();

            for (Object source : list) {
                targetList.add(copyPropertiesWithIgnore(source, classs.newInstance(), ignoreProperties));
            }
            return targetList;
        } catch (Exception var6) {
            throw new BusinessException(BusinessException.EXCEPTIONLEVEL_ERROR, "bean ignoreProperties error");
        }
    }

    public static String[] getNullPropertyNames(Object source, String... ignoreProperties) {
        BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        PropertyDescriptor[] var5 = pds;
        int var6 = pds.length;

        int var7;
        for(var7 = 0; var7 < var6; ++var7) {
            PropertyDescriptor pd = var5[var7];
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result;
        if (ignoreProperties != null) {
            result = ignoreProperties;
            var6 = ignoreProperties.length;

            for(var7 = 0; var7 < var6; ++var7) {
                String ig = result[var7];
                emptyNames.add(ig);
            }
        }

        result = new String[emptyNames.size()];
        return (String[])emptyNames.toArray(result);
    }

    public static <T> T copyPropertiesIgnoreNull(Object source, T target) {
        return copyPropertiesWithIgnore(source, target, getNullPropertyNames(source));
    }

    public static <T> T copyPropertiesIgnoreNullWithProperties(Object source, T target, String... ignoreProperties) {
        return copyPropertiesWithIgnore(source, target, getNullPropertyNames(source, ignoreProperties));
    }
}
