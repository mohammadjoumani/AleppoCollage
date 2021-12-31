package com.example.aleppocollage.ui.mark.studentimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport androidx.lifecycle.viewModelScopeimport com.example.aleppocollage.model.mark.domain.Markimport com.example.aleppocollage.repository.mark.MarkRepositoryimport com.example.aleppocollage.ui.auth.login.student.RegisterStudentStateEventimport com.example.aleppocollage.util.DataStateimport dagger.hilt.android.lifecycle.HiltViewModelimport kotlinx.coroutines.flow.*import kotlinx.coroutines.launchimport javax.inject.Inject@HiltViewModelclass MarkViewModel @Inject constructor(    private val repository: MarkRepository,) : ViewModel() {    var markDataState: MutableLiveData<DataState<List<Mark>>> = MutableLiveData()    fun setStateEvent(markStateEvent: MarkStateEvent, studentId: Int? = null, groupId: Int? = null, month: Int? = null,        studyYear: String? = null) {        viewModelScope.launch {            when (markStateEvent) {                is MarkStateEvent.Mark -> {                    repository.getMarkStudent(studentId!!, groupId!!, month!!, studyYear!!)                        .onEach { dataState ->                            markDataState.value = dataState                        }.launchIn(viewModelScope)                }                is MarkStateEvent.None -> {                    markDataState = MutableLiveData()                }            }        }    }}sealed class MarkStateEvent {    object Mark : MarkStateEvent()    object None : MarkStateEvent()}