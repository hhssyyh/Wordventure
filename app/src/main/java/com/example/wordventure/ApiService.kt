package com.example.wordventure

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

public interface ApiService {
    @POST("/addUsers")
    fun addUsers(@Body user:User): Call<Unit>
    //fun addUsers(@Body users: List<User>) : Call<List<User>>

}