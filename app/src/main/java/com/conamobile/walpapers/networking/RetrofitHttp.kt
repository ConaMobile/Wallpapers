package com.conamobile.walpapers.networking

import com.conamobile.walpapers.util.ApiKeyList
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHttp {
    private const val serverLink = "https://api.unsplash.com/"

    private val client = getClient()
    private val retrofit = getRetrofit(client)

    private fun getRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(serverLink)
            .client(client)
            .build()
    }

    private fun getClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        //print logcat server info
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.header("Authorization", ApiKeyList.getApiKeyAutomatic())
            chain.proceed(builder.build())
        })
        .build()

    val posterService: HomeService = retrofit.create(HomeService::class.java)
}