package com.example.wordventure

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class LoginResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("sessionId") val sessionId: String?,
    @SerializedName("token") val token: String?
)

data class OpenedEpiResponse(
    @SerializedName("epi_no") val epiNo: Int?,
    @SerializedName("total_episode") val numOfEpi: Int?
)

data class OpenedFairyResponse(
    @SerializedName("fairy_no") val fairyNo: Int?
)

data class CardData(
    @SerializedName("eng") val eng : String?,
    @SerializedName("img") val img : String?
)

data class WordData(
    @SerializedName("img") val img : String?,
    @SerializedName("kor") val kr : String?,
    @SerializedName("pronoun") val pron : String?
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

    // 동화 진행 상황
    @GET("/FindUnlockFairy")
    fun openedFairy(@Query("id") userId: String?): Call<OpenedFairyResponse>

    // 에피소드 진행 상황
    @GET("/FindUnlockEpi")
    fun openedEpi(@Query("id") userId: String?, @Query("fairy_no") fairyNo: Int): Call<OpenedEpiResponse>

    // 유저가 모은 카드 데이터
    @GET("/CollectedWords")
    fun getUserCards(@Query("id") userId: String?): Call<List<CardData>>

    // 단어 데이터
    @GET("/Cards")
    fun getWordData(@Query("eng") eng: String?): Call<WordData>

    // 학습한 단어 저장
    @POST("/saveWords")
    fun saveWords(@Query("id") userId: String?, @Query("vocabs") words: List<String>): Call<Unit>

    // 퀴즈 단어 가져오기
    @GET("/getQuizWord")
    fun getQuizWord(@Query("fairy_no") fairyNo: Int?, @Query("epi_no") epiNo: Int?): Call<List<String>>
}
