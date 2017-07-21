package com.example.julia.weatherguide.di;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by julia on 20.07.17.
 */
@Module
public class NetModule {

    private String baseUrl;

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @Provides
    public OkHttpClient provideOkHttpClientWithInterceptor(@NonNull Map<String, String> params) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl.Builder urlBuilder = originalHttpUrl.newBuilder();
                for(Map.Entry<String, String> entry : params.entrySet()) {
                        urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }

                Request.Builder requestBuilder = original.newBuilder()
                        .url(urlBuilder.build());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();
        return okHttpClient;
    }
}
