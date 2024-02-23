package com.theodo.albeniz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfiguration {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(httpRequests ->
                        httpRequests
                                .requestMatchers(
                                        "/tooling/health"
                                )
                                .permitAll()
                                .requestMatchers(
                                        "/v3/api-docs",
                                        "/v3/api-docs/*",
                                        "/swagger-ui.html",
                                        "/swagger-ui/index.html",
                                        "/swagger-ui/*"
                                )
                                .permitAll()
                                .requestMatchers(
                                        "/message",
                                        "/message/*"
                                )
                                .permitAll()
                                .requestMatchers(
                                        "/library",
                                        "/library/**"
                                ).permitAll()
                                .anyRequest()
                                .authenticated()
                );
        return http.build();
    }
}
