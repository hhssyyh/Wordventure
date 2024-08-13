package com.example.wordventure

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class LoginResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("token") val token: String?
)

interface ApiService {
    // ID 중복 확인
    @GET("/checkId")
    fun checkId(@Query("id") id: String): Call<Boolean>

    // 사용자 추가
    @POST("/addUsers")
    fun addUsers(@Body user: User): Call<Unit>

    // 로그인
    @POST("/login")
    fun login(@Body user: User): Call<LoginResponse>
}