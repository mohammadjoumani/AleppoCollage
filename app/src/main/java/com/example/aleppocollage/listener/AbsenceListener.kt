package com.example.aleppocollage.listener

import com.example.aleppocollage.model.absence.domain.Absence

interface AbsenceListener {
    fun onAbsenceChange(absences: List<Absence?>?)
}