package com.theodo.albeniz.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("application")
@Getter
@Setter
public class ApplicationConfig {

    private ApiConfiguration api = new ApiConfiguration();

    @Getter @Setter
    public static class ApiConfiguration {
        int maxCollection = 30;
        boolean ascending = true;
    }
}
