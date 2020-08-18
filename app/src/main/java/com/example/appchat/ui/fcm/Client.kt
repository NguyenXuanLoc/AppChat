package com.example.appchat.ui.fcm

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Client {
    private var retrofit: Retrofit? = null
    fun getClient(url: String?): Retrofit? {
        if (retrofit == null) {
            retrofit =
                Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }
        return retrofit
    }
}