//package mangpo;
//
//import mangpo.server.interceptor.LoginCheckInterceptor;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginCheckInterceptor())
//                .order(1)
//                .excludePathPatterns("/**");
////                .addPathPatterns("/**")
////                .excludePathPatterns("/", "/css/**", "/*.ico", "/error");
//    }
//}
