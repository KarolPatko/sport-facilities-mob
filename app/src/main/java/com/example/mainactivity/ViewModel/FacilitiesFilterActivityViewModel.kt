package com.example.mainactivity.ViewModel

import com.example.mainactivity.Repository.FacilityRepository
import com.example.mainactivity.Repository.UserRepository
import com.example.mainactivity.model.MultipleFacilityItemDto
import com.example.mainactivity.model.Tokens
import okhttp3.RequestBody

class FacilitiesFilterActivityViewModel {

    suspend fun getFacilities(): List<MultipleFacilityItemDto>? {
        return FacilityRepository().getFacilities().body();
    }
}