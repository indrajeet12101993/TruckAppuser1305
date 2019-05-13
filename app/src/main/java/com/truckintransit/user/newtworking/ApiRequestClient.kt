package com.truckintransit.user.newtworking

import com.truckintransit.user.BuildConfig
import com.truckintransit.user.constants.AppConstants
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiRequestClient {

    fun createREtrofitInstance(): ApiRequestEndPoint {
        val rxAdapter= RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
        val builder  = OkHttpClient().newBuilder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .addInterceptor(loggingInterceptor())


        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(rxAdapter)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .baseUrl(AppConstants.BASE_URL)
            .build()

        return retrofit.create(ApiRequestEndPoint::class.java);
    }

    fun loggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }
}