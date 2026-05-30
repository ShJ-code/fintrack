package com.example.fintrackbackend.config;

import com.example.fintrackbackend.security.CurrentUserIdResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CurrentUserIdResolver currentUserIdResolver;

    public WebMvcConfig(CurrentUserIdResolver r) { this.currentUserIdResolver = r; }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserIdResolver);
    }
}
