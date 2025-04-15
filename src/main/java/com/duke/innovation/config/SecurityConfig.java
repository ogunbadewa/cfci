package com.duke.innovation.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)  // Disable Spring Security's form login
                .authorizeRequests()
                .requestMatchers("/**").permitAll();  // Allow all requests

        // Keep your existing headers configuration
        http.headers(headers -> {
            // Disable the default frame options
            headers.frameOptions(FrameOptionsConfig::deny);

            headers.addHeaderWriter((request, response) -> {
                String path = request.getRequestURI();

                if (path.startsWith("/preview")) {
                    response.setHeader("X-Frame-Options", "SAMEORIGIN");
                } else {
                    response.setHeader("X-Frame-Options", "DENY");
                }
            });
        });

        // Keep your existing session management configuration
        http.sessionManagement(config -> config
                .maximumSessions(1)
                .expiredUrl("/login?info=Your session has expired. Please log in again.")
        );

        return http.build();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
