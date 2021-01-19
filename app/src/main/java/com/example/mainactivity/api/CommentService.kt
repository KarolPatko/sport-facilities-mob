package com.example.mainactivity.api

import com.example.mainactivity.model.SingleEventItemDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CommentService {
    @POST("api/comment/facility/{facilityId}")
    suspend fun addComment(@Header("authorization") Authorization: String,
                           @Path("facilityId") facilityId: Long,
                           @Body requestBody: RequestBody): Response<Void>

    companion object{
        operator fun invoke(): CommentService{
            return Retrofit.Builder()
                    .baseUrl("http://192.168.1.171:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(CommentService::class.java)
        }
    }
}

