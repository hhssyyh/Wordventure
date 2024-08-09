package com.example.wordventure

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    // ID 중복 확인
    @GET("/checkId")
    fun checkId(@Query("id") id: String): Call<Boolean>

    // 사용자 추가
    @POST("/addUsers")
    fun addUsers(@Body user: User): Call<Unit>

    // 로그인
    @POST("login")
    fun login(@Body user: User): Call<Unit>
}