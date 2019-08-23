package com.marcus.security;

import com.marcus.base.bean.SecuritySubject;
import com.marcus.base.constants.BaseConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author Marcus.zheng
 * @Date 2019/8/22 11:13
 **/
@Component
public class SessionManager {

    @Resource
    FindByIndexNameSessionRepository<? extends Session> findByIndexNameSessionRepository;
//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    /**
     * 获取当前登录的用户信息
     * @return
     */
    public SecuritySubject getLoginSubject(String sessionId) {
//        Object obj = redisTemplate.opsForHash().get("spring:session:sessions:"+sessionId,"sessionAttr:user");
        //如果走dubbo服务的话必须用这种分布式获取session方式
        Session redisSession = redisOperationsSessionRepository.findById(sessionId);
        if(redisSession==null){
            //如果会话自动过期或第一次加载获取后，会出现空的
            return null;
        }
        SecuritySubject securitySubject = redisSession.getAttribute(BaseConstant.SESSION_USER);
        //如果走fegin 服务调用可以走这种方式
//		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
//		SecuritySubject securitySubject = (SecuritySubject) (request.getSession().getAttribute(BaseConstants.SESSION_FLAG));
        return securitySubject;
    }

    /**
     * 给当前session加上标签(索引)
     * @param request request
     * @param target 标签名
     */
    public void targetSession(HttpServletRequest request, String target){
        request.getSession().setAttribute(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,target);
    }

    /**
     * 根据标签(索引)获取session对象
     * @param target
     * @return 标签下未失效的session
     */
    public Map<String ,? extends Session> getSessionsByTarget(String target){
        //获取索引对应的sessionId
        Map<String ,? extends Session> sessionMap = findByIndexNameSessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,target);
        for (String key:sessionMap.keySet()
        ) {
            //基于Redis的springSession会出现过期却没清除的情况，所以需要手动判断一下。
            if(sessionMap.get(key).isExpired()){
                sessionMap.remove(key);
            }
        }
        return sessionMap;
    }

    /**
     * 统计标签(索引)下session的个数
     * @param target 标签
     * @return 标签下未失效的session的个数
     */
    public Integer countSessionByTarget(String target){
        Map<String ,? extends Session> sessionMap = findByIndexNameSessionRepository.findByIndexNameAndIndexValue(FindByIndexNameSessionRepository.PRINCIPAL_NAME_INDEX_NAME,target);
        Integer count = 0;
        for (Session session:sessionMap.values()
        ) {
            if(!session.isExpired()){
                count++;
            }
        }
        return count;
    }
}
