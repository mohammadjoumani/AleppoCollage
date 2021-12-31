package com.example.aleppocollage.ui.mark.teacher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aleppocollage.model.groupstudent.domain.GroupStudent
import com.example.aleppocollage.repository.deservedgroup.DeservedGroupRepository
import com.example.aleppocollage.repository.mark.MarkRepository
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckedExamViewModel  @Inject constructor(
    private val repository: MarkRepository,
    private val deservedGroupRepository: DeservedGroupRepository,
    ) : ViewModel() {

    var groupStudentsDataState: MutableLiveData<DataState<List<GroupStudent>>> = MutableLiveData()
    var setMarkDataState: MutableLiveData<DataState<String>> = MutableLiveData()

    fun setStateEvent(
        markTeacherStateEvent: MarkTeacherStateEvent,
        groupId: Int? = null,
        examTableId: Int? = null) {
        viewModelScope.launch {
            when (markTeacherStateEvent) {
                is MarkTeacherStateEvent.GroupStudent -> {
                    deservedGroupRepository.getGroupStudentsSelect(groupId!!, examTableId!!)
                        .onEach { dataState ->
                            groupStudentsDataState.value = dataState
                        }.launchIn(viewModelScope)
                }
                is MarkTeacherStateEvent.NoneGroup -> {
                    groupStudentsDataState = MutableLiveData()
                }
            }
        }
    }

    fun setStateEvent(
        markTeacherStateEvent: MarkTeacherStateEvent,
        groupStudents: List<GroupStudent>? = null) {
        viewModelScope.launch {
            when (markTeacherStateEvent) {
                is MarkTeacherStateEvent.Marks -> {
                    repository.updateMark(groupStudents!!).onEach { dataState ->
                        setMarkDataState.value = dataState
                    }.launchIn(viewModelScope)
                }
                is MarkTeacherStateEvent.NoneMarks -> {
                    setMarkDataState = MutableLiveData()
                }
            }
        }
    }
}

sealed class MarkTeacherStateEvent {
    object Marks : MarkTeacherStateEvent()
    object NoneMarks : MarkTeacherStateEvent()
    object GroupStudent : MarkTeacherStateEvent()
    object NoneGroup : MarkTeacherStateEvent()
}