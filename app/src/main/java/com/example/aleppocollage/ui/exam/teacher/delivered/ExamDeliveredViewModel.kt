package com.example.aleppocollage.ui.exam.teacher.delivered

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aleppocollage.model.groupstudent.domain.GroupStudent
import com.example.aleppocollage.repository.deservedgroup.DeservedGroupRepository
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamDeliveredViewModel @Inject constructor(
    private val repository: DeservedGroupRepository,
) : ViewModel() {

    var groupStudentsDataState: MutableLiveData<DataState<List<GroupStudent>>> = MutableLiveData()

    fun setStateEvent(deliveredExamStateEvent: DeliveredExamStateEvent, groupId: Int? = null, examTableId: Int? = null ) {
        viewModelScope.launch {
            when (deliveredExamStateEvent) {
                is DeliveredExamStateEvent.GroupStudent -> {
                    repository.getGroupStudentsSelect(groupId!!, examTableId!!).onEach { dataState ->
                        groupStudentsDataState.value = dataState
                    }.launchIn(viewModelScope)
                }
                is DeliveredExamStateEvent.None -> {
                    groupStudentsDataState = MutableLiveData()
                }
            }
        }
    }


}

sealed class DeliveredExamStateEvent {
    object GroupStudent : DeliveredExamStateEvent()
    object None : DeliveredExamStateEvent()
}