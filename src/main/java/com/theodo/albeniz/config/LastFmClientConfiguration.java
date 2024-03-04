package com.theodo.albeniz.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theodo.albeniz.clients.lastfm.LastFMApiClient;
import com.theodo.albeniz.clients.retrofit.RetrofitHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@Slf4j
public class LastFmClientConfiguration {

    private final ObjectMapper objectMapper;
    private final ApplicationConfig applicationConfig;

    @Bean
    public LastFMApiClient getLastFmClient() {
        return getEsgInvestApiClient();
    }

    private LastFMApiClient getEsgInvestApiClient() {
        return RetrofitHelper.createRetrofitClient(
                applicationConfig.getLastFm().getUrl(),
                LastFMApiClient.class,objectMapper);
    }
}
