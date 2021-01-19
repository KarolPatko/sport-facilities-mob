package com.example.mainactivity.Repository

import com.example.mainactivity.api.UserService
import com.example.mainactivity.model.Tokens
import okhttp3.RequestBody
import retrofit2.Response

class UserRepository {

    suspend fun getTokens(requestBody: RequestBody): Response<Tokens>{
        return UserService().getTokens(requestBody);
    }

}