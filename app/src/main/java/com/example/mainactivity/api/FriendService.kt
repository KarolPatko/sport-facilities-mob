package com.example.mainactivity.api

import com.example.mainactivity.model.FriendDto
import com.example.mainactivity.model.FriendInvitation
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface FriendService {
    @GET("api/friend")
    suspend fun getFriends(@Header("authorization") Authorization: String): Response<List<FriendDto>>

    @GET("api/friend/invitation")
    suspend fun getInvitations(@Header("authorization") Authorization: String): Response<List<FriendInvitation>>

    @POST("api/friend/{friendId}")
    suspend fun deleteFriend(@Header("authorization") Authorization: String, @Path("friendId") friendId: Long): Response<Void>

    @POST("api/friend/accept/friend/{friendId}")
    suspend fun acceptFriend(@Header("authorization") Authorization: String, @Path("friendId") friendId: Long): Response<Void>

    @POST("api/friend")
    suspend fun inviteFriend(@Header("authorization") Authorization: String, @Body requestBody: RequestBody): Response<Void>
}