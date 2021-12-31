package com.example.aleppocollage.ui.home.student

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aleppocollage.model.attendance.domain.Attendance
import com.example.aleppocollage.repository.attendance.AttendanceRepository
import com.example.aleppocollage.ui.attendance.student.AttendanceStudentStateEvent
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeStudentViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,

    ) : ViewModel() {

    var attendanceDataState: MutableLiveData<DataState<Attendance>> = MutableLiveData()

    fun setStateEvent(homeStudentStateEvent: HomeStudentStateEvent, studentId: Int? = null, date: String? = null) {
        viewModelScope.launch {
            when (homeStudentStateEvent) {
                is HomeStudentStateEvent.Attendacne -> {
                    attendanceRepository.getAttendanceStudent(studentId!!, date!!).onEach { dataState ->
                        attendanceDataState.value = dataState
                    }.launchIn(viewModelScope)
                }
                is HomeStudentStateEvent.NoneAttendacne -> {
                    attendanceDataState = MutableLiveData()
                }
            }
        }
    }

}

sealed class HomeStudentStateEvent {
    object Attendacne : HomeStudentStateEvent()
    object NoneAttendacne : HomeStudentStateEvent()
}