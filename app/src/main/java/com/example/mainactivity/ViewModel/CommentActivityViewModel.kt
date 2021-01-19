package com.example.mainactivity.ViewModel

import com.example.mainactivity.Repository.CommentRepository
import com.example.mainactivity.Repository.FacilityRepository
import com.example.mainactivity.model.SingleFacilityItemDto
import okhttp3.RequestBody

class CommentActivityViewModel {
    suspend fun addComment(Authorization: String,  facilityId: Long, requestBody: RequestBody): Int {
        return CommentRepository().addComment(Authorization, facilityId, requestBody).code();
    }
}

