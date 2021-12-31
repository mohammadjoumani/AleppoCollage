package com.example.aleppocollage.ui.main.model

import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.model.user.domain.Teacher

class ProfileInfo(
    val userType: String,
    val student: Student? = null,
    val teacher: Teacher? = null,
    var state: Boolean,
)