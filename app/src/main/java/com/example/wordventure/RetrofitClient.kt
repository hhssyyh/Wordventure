package com.example.wordventure

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

// OkHttpClient 설정
val client = OkHttpClient.Builder()
    .connectTimeout(100, TimeUnit.SECONDS)  // 연결 타임아웃 시간 설정
    .readTimeout(100, TimeUnit.SECONDS)     // 읽기 타임아웃 시간 설정
    .writeTimeout(100, TimeUnit.SECONDS)    // 쓰기 타임아웃 시간 설정
    .build()

//object RetrofitClient {
//    private const val BASE_URL = "http://192.168.0.19:3306/"
//
//    private val retrofit: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//
//    val apiService: ApiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }
//}

// 서버 로그 보기
object RetrofitClient {
    private const val BASE_URL = "http://192.168.0.31:3306/"

    private val logging by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        interceptor
    }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}