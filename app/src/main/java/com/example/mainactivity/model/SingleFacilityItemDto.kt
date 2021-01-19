package com.example.mainactivity.model

class SingleFacilityItemDto(
    var facilityId: Long,
    var lat: Double,
    var lon: Double,
    var address: String,
    var surface: String,
    var district: String,
    var description: String,
    var animatorId: Long,
    var sport: Set<SportDto>,
    var comment: Set<CommentDto>,
    var rateAvg: Double){
}