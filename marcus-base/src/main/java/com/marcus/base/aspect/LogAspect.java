package com.marcus.base.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;


/**
 * @ClassName LogAspect
 * @Description
 * @Author Marcus.zheng
 * @Date 2019/8/18 10:41
 * @Version 1.0
 **/
@Aspect
@Component
@ConditionalOnExpression("${system.open.logAspect:false}")
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    public LogAspect() {
    }

    @Pointcut("execution(* com.marcus..*.service..*.*(..)) || execution(* com.marcus..*.dao..*.*(..))")
    public void logPointCut(){}

//    @Around("logPointCut()")
//    public Object around(ProceedingJoinPoint joinPoint)
}
