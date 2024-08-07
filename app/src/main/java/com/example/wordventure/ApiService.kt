package com.example.wordventure

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // ID 중복 확인
    @POST("/check-id")
    fun checkId(@Body id: String): Call<Boolean>

    // 사용자 추가
    @POST("/addUsers")
    fun addUsers(@Body user: User): Call<Unit>

    // 로그인
    @POST("login")
    fun login(@Body user: User): Call<Unit>
}