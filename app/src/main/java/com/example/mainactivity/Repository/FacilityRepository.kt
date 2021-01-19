package com.example.mainactivity.Repository

import com.example.mainactivity.api.FacilityService
import com.example.mainactivity.api.UserService
import com.example.mainactivity.model.MultipleFacilityItemDto
import com.example.mainactivity.model.SingleFacilityItemDto
import com.example.mainactivity.model.Tokens
import okhttp3.RequestBody
import retrofit2.Response

class FacilityRepository {
    suspend fun getFacilities(): Response<List<MultipleFacilityItemDto>> {
        return FacilityService().getFacilities();
    }
    suspend fun getFacility(facilityId: Long): Response<SingleFacilityItemDto> {
        return FacilityService().getFacility(facilityId);
    }
}