package com.marcus.startup.handler;

import com.alibaba.fastjson.JSON;
import com.zzl.common.utils.ExceptionUtils;
import com.zzl.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 异常统一处理
 * @author CharlesLU
 * @since 2019-07-12 15:30
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonExceptionHandlerResolver implements HandlerExceptionResolver {

    private final Logger log = LoggerFactory.getLogger(getClass());



    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        log.error("request [" + request.getRequestURI() + "] happened error ",ex);

        ModelAndView mv = new ModelAndView();

        if ((request.getHeader("accept").contains("application/json") ||
                (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").contains("XMLHttpRequest")))) {
            try
            {
                Result errorResult = ExceptionUtils.getErrorResult(ex);
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(errorResult));
                writer.flush();
                writer.close();

                //异常处理完成后，返回空的ModelAndView 防止异常被继续抛出报错
                return mv;
            }
            catch (IOException e)
            {
                log.error("request [" + request.getRequestURI() + "] happened error ",e);
            }
        }

        //对于非ajax请求，我们都统一跳转到500.html页面，后续是否追加错误信息
        mv.setViewName("error/500");
        return mv;

    }
}
