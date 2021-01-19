package com.example.mainactivity.api

import com.example.mainactivity.model.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserService {

    @POST("api/token")
    suspend fun getTokens(@Body requestBody: RequestBody): Response<Tokens>

    @POST("api/user")
    suspend fun register(@Body requestBody: RequestBody): Response<User>

    @GET("api/user/all")
    suspend fun getUsersPrefix(@Header("Authorization") Authorization: String, @Query("prefix") prefix: String): Response<Array<PrefixUserItem>>

    companion object{
        operator fun invoke(): UserService{
            return Retrofit.Builder()
                    .baseUrl("http://192.168.1.171:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(UserService::class.java)
        }
    }
}