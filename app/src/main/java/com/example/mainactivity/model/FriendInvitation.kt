package com.example.mainactivity.model

class FriendInvitation(
    var userId: Long,
var username: String,
var photo: String,
var name: String,
var lastName: String,
var friendId: Long,
var inviter: Boolean) {
}