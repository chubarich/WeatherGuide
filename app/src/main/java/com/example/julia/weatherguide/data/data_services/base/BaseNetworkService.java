package com.example.julia.weatherguide.data.data_services.base;

import com.example.julia.weatherguide.utils.Preconditions;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class BaseNetworkService<S> {

    private S dataService;


    public BaseNetworkService(String baseUrl, OkHttpClient okHttpClient){
        Preconditions.nonNull(baseUrl, okHttpClient);

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();
        this.dataService = createService(retrofit);
    }


    protected S getService() {
        return this.dataService;
    }

    protected abstract S createService(Retrofit retrofit);

}
