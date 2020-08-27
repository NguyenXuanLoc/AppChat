package com.example.appchat.ui.fcm

import com.example.appchat.common.Key
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    @Headers(
        "Content-Type:${Key.CONTENT_TYPE}",
        "Authorization:key=${Key.KEY_AUTH}"
    )
    @POST("fcm/send")
    fun sendNotification(@Body body: NotificationSender?): Single<MyResponse>
}