package com.theodo.albeniz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.theodo.albeniz.repositories.UserEntityRepository;
import com.theodo.albeniz.services.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration {

  private final UserEntityRepository userEntityRepository;

  @Bean
  protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(Customizer.withDefaults())

        .authorizeHttpRequests(httpRequests -> httpRequests
            .requestMatchers(
                "/tooling/health")
            .permitAll()
            .requestMatchers(
                "/v3/api-docs",
                "/v3/api-docs/*",
                "/swagger-ui.html",
                "/swagger-ui/index.html",
                "/swagger-ui/*")
            .permitAll()
            .requestMatchers(
                "/message",
                "/message/*")
            .permitAll()
            .requestMatchers(
                "/library",
                "/library/**")
            .permitAll()
            .anyRequest()
            .authenticated())
        .userDetailsService(new UserDetailsServiceImpl(userEntityRepository))
        .formLogin(conf -> conf
            .usernameParameter("username")
            .passwordParameter("password")
            .permitAll())
        .logout(conf -> conf.permitAll(false));
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}