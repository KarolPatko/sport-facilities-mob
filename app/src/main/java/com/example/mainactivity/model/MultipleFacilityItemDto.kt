package com.example.mainactivity.model

class MultipleFacilityItemDto(
    var facilityId: Long,
    var lat: Double,
    var lon: Double,
    var address: String,
    var surface: String,
    var district: String,
    var sport: List<SportDto>,
    var animatorId: Long) {
}