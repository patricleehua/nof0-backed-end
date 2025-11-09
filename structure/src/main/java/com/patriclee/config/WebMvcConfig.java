//package com.patriclee.config;
//
//import cn.dev33.satoken.context.SaHolder;
//import cn.dev33.satoken.interceptor.SaInterceptor;
//import cn.dev33.satoken.router.SaRouter;
//import cn.dev33.satoken.stp.StpUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@Slf4j
//public class WebMvcConfig implements WebMvcConfigurer {
//    /**
//     * 解决跨域
//     * 这只解决了浏览器对跨域请求的限制,还要在controller配置
//     * @param registry
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        log.info("跨域配置 CORS...");
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*") // 配置域名可以用allowedOriginPatterns
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS")
//                .allowedHeaders("*")
//                .exposedHeaders("Authorization","iv") // 添加此行以暴露 Authorization/iv 响应头
//                .maxAge(3600);
//    }
//    /**
//     * 自定义拦截器
//     *
//     * @param registry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        log.info("注册自定义拦截器...");
//        // 注册路由拦截器，自定义认证规则
//        registry.addInterceptor(new SaInterceptor(handler -> {
//            SaRouter.match("/**")
//                    .notMatch(
//                            "/system/user/login",
//                            "/wx/user/loginByAccount",
//                            "/openapi/v1/auth/*",
//                            "/favicon.ico",
//                            "/v3/**",
//                            "/swagger-ui/**",
//                            "/common/open/**",
//                            "/banner/open/**",
//                            "/test/**"
//                    )
//                    .check(r -> {
//                        if (!"OPTIONS".equalsIgnoreCase(SaHolder.getRequest().getMethod())) {
//                            StpUtil.checkLogin();
//                        }
//                    });
//        }));
//    }
//}
