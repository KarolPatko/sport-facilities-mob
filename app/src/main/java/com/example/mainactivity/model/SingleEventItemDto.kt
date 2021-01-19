package com.example.mainactivity.model

class SingleEventItemDto(
    var dateStart: String,
    var dateEnd: String,
    var description: String,
    var user: UserForSingleEventItemDto,
    var facility: FacilityForSingleEventItemDto,
    var sport: String,
    var participants: Set<ParticipantDto>
){

    override fun toString(): String {
        return "$dateStart $dateEnd";
    }
}