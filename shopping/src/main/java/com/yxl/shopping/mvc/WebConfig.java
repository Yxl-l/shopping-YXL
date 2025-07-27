//package com.yxl.shopping.mvc;//package com.exam.mvc;
//
//
//import com.yxl.shopping.interceptor.TokenInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Autowired
//    private TokenInterceptor tokenInterceptor;
//    //  拦截器
//
// @Override
// public void addInterceptors(InterceptorRegistry registry) {
//     registry.addInterceptor(tokenInterceptor).addPathPatterns("/**").excludePathPatterns("/user/login");//设置不拦截的请求路径
// }
//}
//
