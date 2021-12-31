package com.example.aleppocollage.ui.exam.teacher

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aleppocollage.model.deservedgroup.domain.DeservedGroup
import com.example.aleppocollage.model.exam.domain.Exam
import com.example.aleppocollage.repository.deservedgroup.DeservedGroupRepository
import com.example.aleppocollage.repository.exam.ExamRepository
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamViewModel  @Inject constructor(
    private val repository: ExamRepository,
    private val repositoryDeserved: DeservedGroupRepository,
) : ViewModel() {

    var stateCallback = MutableLiveData(0)

    var groupsDataState: MutableLiveData<DataState<List<DeservedGroup>>> = MutableLiveData()
    var examsDataState: MutableLiveData<DataState<List<Exam>>> = MutableLiveData()

    fun setGroupStateEvent(examStateEvent: ExamStateEvent, teacherId: Int? = null, year: String? = null ) {
        viewModelScope.launch {
            when (examStateEvent) {
                is ExamStateEvent.Group -> {
                    repositoryDeserved.getDeservedGroups(year!!, teacherId!!).onEach { dataState ->
                        groupsDataState.value = dataState
                    }.launchIn(viewModelScope)
                }
                is ExamStateEvent.NoneGroup -> {
                    groupsDataState = MutableLiveData()
                }
            }
        }
    }

    fun setExamStateEvent(examStateEvent: ExamStateEvent, subjectId:Int? = null, groupId: Int? = null ) {
        viewModelScope.launch {
            when (examStateEvent) {
                is ExamStateEvent.Exam -> {
                    repository.getExams(subjectId!!, groupId!!).onEach { dataState ->
                        examsDataState.value = dataState
                    }.launchIn(viewModelScope)
                }
                is ExamStateEvent.NoneExam -> {
                    examsDataState = MutableLiveData()
                }
            }
        }
    }

}

sealed class ExamStateEvent {
    object Exam : ExamStateEvent()
    object Group : ExamStateEvent()
    object NoneGroup : ExamStateEvent()
    object NoneExam : ExamStateEvent()
}