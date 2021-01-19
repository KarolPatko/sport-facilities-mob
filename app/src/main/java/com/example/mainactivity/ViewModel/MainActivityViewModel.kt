package com.example.mainactivity.ViewModel

import androidx.lifecycle.ViewModel
import com.example.mainactivity.Repository.UserRepository
import com.example.mainactivity.model.Tokens
import okhttp3.RequestBody

class MainActivityViewModel : ViewModel() {

    suspend fun getTokens(requestBody: RequestBody): Tokens? {
        return UserRepository().getTokens(requestBody).body();
    }
}