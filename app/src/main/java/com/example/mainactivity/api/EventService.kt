package com.example.mainactivity.api

import com.example.mainactivity.model.MultipleEventItemDto
import com.example.mainactivity.model.MultipleFacilityItemDto
import com.example.mainactivity.model.SingleEventItemDto
import com.example.mainactivity.model.SingleFacilityItemDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface EventService {
    @GET("api/event/all")
    suspend fun getEvents(): Response<List<MultipleEventItemDto>>

    @GET("api/event/{eventId}")
    suspend fun getEvent(@Path("eventId") eventId: Long): Response<SingleEventItemDto>

    @GET("api/event/{eventId}")
    suspend fun getEvent(@Path("eventId") eventId: String): Response<SingleEventItemDto>

    @GET("api/event/my/all")
    suspend fun getMyEvents(@Header("Authorization") Authorization: String): Response<List<MultipleEventItemDto>>

    @POST("api/event")
    suspend fun addEvent(@Header("authorization") Authorization: String, @Body requestBody: RequestBody): Response<Void>

    @DELETE("api/event/{eventId}")
    suspend fun deleteEvent(@Header("authorization") Authorization: String, @Path("eventId") eventId: Long): Response<Void>

    @POST("api/event/{eventId}/participant/{userId}/invite")
    suspend fun invite(@Header("authorization") Authorization: String, @Path("eventId") eventId: Long, @Path("userId") userId: Long): Response<Void>


    @POST("api/event/{eventId}/participant/{userId}/delete")
    suspend fun delFromEvent(@Path("eventId") eventId: Long, @Path("userId") userId: Long): Response<Void>

}