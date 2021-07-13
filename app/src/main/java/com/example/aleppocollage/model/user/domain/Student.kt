package com.example.aleppocollage.model.user.domain

data class Student(
    val id:Int,
    val name: String,
    val password: String,
    val groupID:Int,
    val groupLevel:String,
    val groupName:String,
    val groupGender:String
)