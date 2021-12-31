package com.example.aleppocollage.ui.mark.maxmarkimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport androidx.lifecycle.viewModelScopeimport com.example.aleppocollage.model.mark.domain.Markimport com.example.aleppocollage.repository.mark.MarkRepositoryimport com.example.aleppocollage.ui.auth.login.student.RegisterStudentStateEventimport com.example.aleppocollage.ui.mark.student.MarkStateEventimport com.example.aleppocollage.util.DataStateimport dagger.hilt.android.lifecycle.HiltViewModelimport kotlinx.coroutines.flow.*import kotlinx.coroutines.launchimport javax.inject.Inject@HiltViewModelclass MaxMarkViewModel @Inject constructor(    private val repository: MarkRepository,) : ViewModel() {    var maxMarkDataState: MutableLiveData<DataState<Unit>> = MutableLiveData()    fun setStateEvent(maxMarkEvent: MaxMarkEvent, id: Int? = null, maxMark: Int? = null, description: String? = null ) {        viewModelScope.launch {            when (maxMarkEvent) {                is MaxMarkEvent.Max -> {                    repository.updateMaxMark(id!!, maxMark!!, description!!)                        .onEach { dataState ->                            maxMarkDataState.value = dataState                        }.launchIn(viewModelScope)                }                is MaxMarkEvent.None -> {                    maxMarkDataState = MutableLiveData()                }            }        }    }}sealed class MaxMarkEvent {    object Max : MaxMarkEvent()    object None : MaxMarkEvent()}