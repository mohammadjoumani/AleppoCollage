package com.example.aleppocollage.ui.util.sharedviewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aleppocollage.ui.main.model.ProfileInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    var date: MutableLiveData<String> = MutableLiveData()

    var showProfileInfo = MutableLiveData(ProfileInfo("",null,null,false))

    var isDissconnect = MutableLiveData<Boolean>()

    var stateLoading = MutableLiveData<Boolean>()
    var textLoading = MutableLiveData<String>()

    var stateStartApp = MutableLiveData(0)

    var markValue = MutableLiveData(0)

}