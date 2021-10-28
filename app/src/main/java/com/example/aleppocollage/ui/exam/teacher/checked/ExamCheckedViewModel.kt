package com.example.aleppocollage.ui.exam.teacher.checked

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aleppocollage.model.exam.domain.Exam
import com.example.aleppocollage.model.groupstudent.domain.GroupStudent
import com.example.aleppocollage.repository.deservedgroup.DeservedGroupRepository
import com.example.aleppocollage.repository.exam.ExamRepository
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckedExamViewModel  @Inject constructor(
    private val repository: DeservedGroupRepository,
    ) : ViewModel() {

    var groupStudentsDataState: MutableLiveData<DataState<List<GroupStudent>>> = MutableLiveData()

    fun setStateEvent(checkExamStateEvent: CheckExamStateEvent, groupId: Int? = null, examTableId: Int? = null ) {
        viewModelScope.launch {
            when (checkExamStateEvent) {
                is CheckExamStateEvent.GroupStudent -> {
                    repository.getGroupStudentsSelect(groupId!!, examTableId!!).onEach { dataState ->
                        groupStudentsDataState.value = dataState
                    }.launchIn(viewModelScope)
                }
                is CheckExamStateEvent.None -> {
                    groupStudentsDataState = MutableLiveData()
                }
            }
        }
    }


}

sealed class CheckExamStateEvent {
    object GroupStudent : CheckExamStateEvent()
    object None : CheckExamStateEvent()
}