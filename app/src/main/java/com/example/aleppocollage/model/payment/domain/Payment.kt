package com.example.aleppocollage.model.payment.domain

data class Payment(
    var id: Int,
    var payNo: Int,
    var date: String,
    var dateForMonth: String,
    var pay: Int
)