package com.example.mainactivity.ViewModel

import com.example.mainactivity.Repository.FacilityRepository
import com.example.mainactivity.model.MultipleFacilityItemDto
import com.example.mainactivity.model.SingleFacilityItemDto

class FacilityActivityViewModel {

    suspend fun getFacility(facilityId: Long): SingleFacilityItemDto? {
        return FacilityRepository().getFacility(facilityId).body();
    }
}