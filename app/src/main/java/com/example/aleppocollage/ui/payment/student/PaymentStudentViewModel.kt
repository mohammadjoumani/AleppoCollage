package com.example.aleppocollage.ui.payment.student

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aleppocollage.model.payment.domain.Payment
import com.example.aleppocollage.repository.payment.PaymentRepository
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentStudentViewModel  @Inject constructor(
    private val repository: PaymentRepository,
) : ViewModel() {

    var paymentsDataState: MutableLiveData<DataState<List<Payment>>> = MutableLiveData()
    var costDataState: MutableLiveData<DataState<Int>> = MutableLiveData()

    fun setPaymentStateEvent(paymentStudentStateEvent: PaymentStudentStateEvent, studentId: Int? = null, year: String? = null) {
        viewModelScope.launch {
            when (paymentStudentStateEvent) {
                is PaymentStudentStateEvent.Payment -> {
                    repository.getPaymentStudent(studentId!!, year!!).onEach { dataState ->
                        paymentsDataState.value = dataState
                    }.launchIn(viewModelScope)
                }
                is PaymentStudentStateEvent.NonePayment -> {
                    paymentsDataState = MutableLiveData()
                }
            }
        }
    }

    fun setCostStateEvent(paymentStudentStateEvent: PaymentStudentStateEvent, studentId: Int? = null, groupId: Int? = null) {
        viewModelScope.launch {
            when (paymentStudentStateEvent) {
                is PaymentStudentStateEvent.Cost -> {
                    repository.getCostStudent(studentId!!, groupId!!).onEach { dataState ->
                        costDataState.value = dataState
                    }.launchIn(viewModelScope)
                }
                is PaymentStudentStateEvent.NoneCost -> {
                    costDataState = MutableLiveData()
                }
            }
        }
    }

}

sealed class PaymentStudentStateEvent {
    object Payment : PaymentStudentStateEvent()
    object Cost : PaymentStudentStateEvent()
    object NonePayment : PaymentStudentStateEvent()
    object NoneCost : PaymentStudentStateEvent()
}