package com.example.aleppocollage.ui.testteacher

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
    var deservedGroup: MutableLiveData<Resource<ArrayList<DeservedGroup>>>? = null
    private val groupList = ArrayList<DeservedGroup>()


    fun getDeservedGroup(studyYear: String, teacherID: Int) = viewModelScope.launch {
        deservedGroup = MutableLiveData()
        groupList.clear()
        testRepository.getDeservedGroups(studyYear, teacherID).onStart {
                deservedGroup!!.postValue(Resource.Loading(true))
            }.catch {
                it.message?.let { message ->
                    deservedGroup!!.postValue(Resource.Error(message))
                }
            }.collect { _group ->
                if (_group == null) {
                    deservedGroup!!.postValue(Resource.FAILURE("name or password"))
                } else {
                    groupList.add(_group)
                }
                deservedGroup!!.postValue(Resource.Success(groupList))
            }
    }
}