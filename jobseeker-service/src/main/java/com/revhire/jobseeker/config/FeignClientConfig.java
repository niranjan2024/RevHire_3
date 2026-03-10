package com.revhire.jobseeker.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (attributes != null) {
                    String authHeader = attributes.getRequest().getHeader("Authorization");
                    if (authHeader != null) {
                        template.header("Authorization", authHeader);
                    }

                    String userId = attributes.getRequest().getHeader("X-User-Id");
                    if (userId != null) {
                        template.header("X-User-Id", userId);
                    }

                    String username = attributes.getRequest().getHeader("X-Username");
                    if (username != null) {
                        template.header("X-Username", username);
                    }

                    String role = attributes.getRequest().getHeader("X-User-Role");
                    if (role != null) {
                        template.header("X-User-Role", role);
                    }
                }
            }
        };
    }
}
