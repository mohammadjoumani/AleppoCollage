package com.example.aleppocollage.ui.testTeacher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aleppocollage.model.test.domain.DeservedGroup
import com.example.aleppocollage.repository.test.TestRepository
import com.example.aleppocollage.util.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class TestViewModel : ViewModel() {

    private val testRepository = TestRepository()
    private val _deservedGroup = MutableLiveData<Resource<ArrayList<DeservedGroup>>>()
    val deservedGroup: LiveData<Resource<ArrayList<DeservedGroup>>> = _deservedGroup
    private val groupList = ArrayList<DeservedGroup>()


    fun getDeservedGroup(studyYear: String, teacherID: Int) {
        viewModelScope.launch {
            groupList.clear()
            testRepository.getDeservedGroups(studyYear, teacherID)
                .onStart {
                    _deservedGroup.postValue(Resource.Loading(true))
                }
                .catch {
                    it.message?.let { message ->
                        _deservedGroup.postValue(Resource.Error(message))
                    }
                }
                .collect { _group ->
                    if (_group == null) {
                        _deservedGroup.postValue(Resource.FAILURE("name or password"))
                    } else {
                        groupList.add(_group)
                    }
                    _deservedGroup.postValue(Resource.Success(groupList))
                }
        }
    }
}