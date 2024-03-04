package com.theodo.albeniz.clients.retrofit;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitHelper {
    public static <T> T createRetrofitClient(String baseUrl, Class<T> clazz, ObjectMapper objectMapper) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .client(new OkHttpClient())
            .build();

        return retrofit.create(clazz);
    }
}
