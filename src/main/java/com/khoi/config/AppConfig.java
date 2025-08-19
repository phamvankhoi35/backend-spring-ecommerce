package com.khoi.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration // sử dụng để đánh dấu một class là nguồn cấu hình, pring sẽ quét các class này để tìm và tạo các bean dựa trên các phương thức được đánh dấu bằng @Bean
@EnableWebSecurity // sử dụng để kích hoạt và cấu hình bảo mật web
public class AppConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Dòng dưới cho biết không lưu session phía server
                // Phù hợp với REST API + JWT (vì auth nằm ở token, không cần session)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Phân quyền truy cập endpoint
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/**").authenticated()      // cần đăng nhập và yêu cầu JWT
                        .requestMatchers(("/api/products/*/reviews")).permitAll()   // ai cũng truy cập được
                        .anyRequest().permitAll()                                   // mặc định cho truy cập các route còn lại
                )

                // Thêm bộ lọc kiểm tra JWT
                // JwtTokenValidator() là custom filter tự viết, Chèn vào trước BasicAuthenticationFilter
                // Mục đích: kiểm tra token JWT trong header → nếu hợp lệ thì cho vào SecurityContext
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)

                // Tắt CSRF, CSRF thường dùng cho web form
                .csrf(csrf -> csrf.disable())

                // Bật CORS (nếu frontend gọi từ domain khác) nhằm hỗ trợ frontend gọi API
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration cfg = new CorsConfiguration();

                // Cho phép mọi domain gọi API
                cfg.setAllowedOrigins(Collections.singletonList("*"));
                cfg.setAllowedOrigins(Collections.singletonList("*"));
                cfg.setAllowedOrigins(Collections.singletonList("*"));

                // Cho phép gửi cookie, Authorization header, ...
                cfg.setAllowCredentials(true);

                // Cho phép frontend đọc được header Authorization từ response
                cfg.setExposedHeaders(Collections.singletonList("Authorization"));
                cfg.setMaxAge(3600L);
                return cfg;
            }
        };
    }

//    @Bean
//    public CorsConfigurationSource getCorsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//
//        // dùng allowedOriginPatterns thay vì allowedOrigins khi dùng "*"
//        config.setAllowedOriginPatterns(List.of("*"));
//        // Hoặc chỉ định rõ tên miền frontend
//        // config.setAllowedOrigins(List.of("http://localhost:3000"));
//
//        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        config.setAllowedHeaders(List.of("*"));
//        config.setExposedHeaders(List.of("Authorization"));
//        config.setAllowCredentials(true);
//        config.setMaxAge(3600L);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return source;
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
