package com.example.aleppocollage.model.exam.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exam(
    val id: Int,
    val name: String,
    val description: String,
    val date: String,
    val deliveredDate: String,
    val maxMark: Int,
    val status: String
): Parcelable