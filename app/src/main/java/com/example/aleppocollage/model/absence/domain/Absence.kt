package com.example.aleppocollage.model.absence.domain

data class Absence(
    var id: Int,
    var studentID: Int,
    var student: String,
    var attendenceValue: String?,
    var date: String?,
    var status: String?,
    var note: String?,

    var session0: String?,
    var session1: String?,
    var session2: String?,
    var session3: String?,
    var session4: String?,
    var session5: String?,
    var session6: String?,
    var session7: String?,
    var session8: String?,
    var session9: String?
)