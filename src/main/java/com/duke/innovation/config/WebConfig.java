package com.duke.innovation.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static class CacheControlInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            // Set cache control headers
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            return true;
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CacheControlInterceptor())
                .addPathPatterns("/login", "/register");

        registry.addInterceptor(new SessionInterceptor())
                .excludePathPatterns("/login", "/logout", "/register",
                        "/css/**", "/js/**", "/images/**");
    }

    public static class SessionInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
            // Check if the user is authenticated for protected resources
            if (request.getRequestURI().startsWith("/follow") ||
                    request.getRequestURI().startsWith("/add-project")) {
                if (request.getSession(false) == null ||
                        request.getSession().getAttribute("user") == null) {
                    response.sendRedirect("/login?info=Please login to continue");
                    return false;
                }
            }
            return true;
        }
    }
}
