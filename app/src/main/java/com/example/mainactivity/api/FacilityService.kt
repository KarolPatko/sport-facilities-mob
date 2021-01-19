package com.example.mainactivity.api

import com.example.mainactivity.model.MultipleFacilityItemDto
import com.example.mainactivity.model.SingleFacilityItemDto
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface FacilityService {
    @GET("api/facility")
    suspend fun getFacilities(): Response<List<MultipleFacilityItemDto>>

    @GET("api/facility/{facilityId}")
    suspend fun getFacility(@Path("facilityId") friendId: Long): Response<SingleFacilityItemDto>

    companion object{
        operator fun invoke(): FacilityService{
            return Retrofit.Builder()
                    .baseUrl("http://192.168.1.171:8080")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(FacilityService::class.java)
        }
    }
}