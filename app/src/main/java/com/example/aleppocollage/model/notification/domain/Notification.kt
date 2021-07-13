package com.example.aleppocollage.model.notification.domain

data class Notification(
    var id: Int,
    var title: String,
    var content: String,
    var type: String,
    var info: String,
    var date: String,
    var studentID: Int,
    var teacherID: Int,
    var groupID: Int
)