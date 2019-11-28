package cn.celess.blog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author : xiaohai
 * @date : 2019/03/30 19:55
 * 跨域
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://celess.cn");
        config.addAllowedOrigin("http://www.celess.cn");
        config.addAllowedOrigin("https://celess.cn");
        config.addAllowedOrigin("https://www.celess.cn");
        // 本地调试时的跨域
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedOrigin("http://127.0.0.1:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.setAllowCredentials(true);
        config.setMaxAge(10800L);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
