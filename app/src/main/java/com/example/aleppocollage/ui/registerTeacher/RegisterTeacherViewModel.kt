package com.example.aleppocollage.ui.registerTeacherimport android.util.Logimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport androidx.lifecycle.viewModelScopeimport com.example.aleppocollage.model.user.domain.Teacherimport com.example.aleppocollage.repository.register.TeacherRepositoryimport com.example.aleppocollage.util.Resourceimport kotlinx.coroutines.flow.catchimport kotlinx.coroutines.flow.collectimport kotlinx.coroutines.flow.onStartimport kotlinx.coroutines.launchimport java.util.ArrayListclass RegisterTeacherViewModel : ViewModel() {    private val teacherRepository = TeacherRepository()    private val _teachers = MutableLiveData<Resource<ArrayList<Teacher>>>()    val teachersInfo: LiveData<Resource<ArrayList<Teacher>>> = _teachers    private val teacherList = ArrayList<Teacher>()    fun getAllTeacher() {        viewModelScope.launch {            teacherList.clear()            teacherRepository.getAllTeacher()                .onStart {                    _teachers.postValue(Resource.Loading(true))                }                .catch {                    it.message?.let { message ->                        _teachers.postValue(Resource.Error(message))                    }                }                .collect { _teacher ->                    if (_teacher == null) {                        _teachers.postValue(Resource.FAILURE("name or password"))                    } else {                        teacherList.add(_teacher)                        Log.d("teacher",_teacher.name)                    }                    _teachers.postValue(Resource.Success(teacherList))                }        }    }}