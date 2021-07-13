package com.example.aleppocollage.model.mark.domain

data class Mark(
    val subjectName: String,
    val examName: String,
    val maxMark: Int,
    val date: String,
    val description: String,
    val grade: Int
)