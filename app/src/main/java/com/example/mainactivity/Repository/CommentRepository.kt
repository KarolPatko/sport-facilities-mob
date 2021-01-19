package com.example.mainactivity.Repository

import com.example.mainactivity.api.CommentService
import com.example.mainactivity.api.FacilityService
import com.example.mainactivity.model.SingleFacilityItemDto
import com.example.mainactivity.utils.Constants
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path

class CommentRepository {
    suspend fun addComment(Authorization: String,  facilityId: Long, requestBody: RequestBody): Response<Void> {
        return CommentService().addComment(Authorization, facilityId, requestBody);
    }
}


