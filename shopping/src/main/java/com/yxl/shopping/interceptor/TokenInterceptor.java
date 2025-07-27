package com.yxl.shopping.interceptor;


import com.yxl.shopping.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       log.info("令牌校验执行了");
        String token = request.getHeader("token"); //3. 获取请求头中的令牌（token）。
        Map<String,Object> map = JwtUtils.parseJWT(token);
        if(ObjectUtils.isEmpty(token) || map==null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.info("令牌不合法不放行");
            return false;
        }
        log.info("令牌合法 放行");
        return true;
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//       log.info("postHandle ... 方法执行完后清空当前线程绑定的id");
//       很少使用
//    }


}
