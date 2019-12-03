package com.kt.gigaiot_sdk.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static Retrofit retrofit = null;

    // OPEN API BASE ADDRESS
    private static final String OPEN_API_BASE_ADDRESS = "http://iotmakers.kt.com/";

    // 인터페이스를 설정할 때마다 호출되는 메소드
    // 이 메소드를 이용하여 Retrofit에 접근하고, HTTP method를 이용하게 된다.
    public static Retrofit getAUTHClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(OPEN_API_BASE_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
