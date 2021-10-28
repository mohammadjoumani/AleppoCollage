package com.example.aleppocollage.ui.payment.student

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentPaymentStudentBinding
import com.example.aleppocollage.model.payment.domain.Payment
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.mark.student.adapter.YearSpinnerAdapter
import com.example.aleppocollage.ui.payment.adapter.PaymentRecyclerAdapter
import com.example.aleppocollage.ui.util.loading.LoadingViewModel
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentStudentFragment : Fragment(R.layout.fragment_payment_student) {

    ///region Variables

    private var years: ArrayList<String> = ArrayList()
    private var payments: ArrayList<Payment> = ArrayList()
    private var year = ""

    private var paid = 0

    //endregion

    ///region ViewModel & Binding

    private val viewModel: PaymentStudentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentPaymentStudentBinding? = null
    private val binding get() = _binding!!

    ///endregion

    private lateinit var paymentRecyclerAdapter: PaymentRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPaymentStudentBinding.bind(view)

        init()

        binding.apply {

            cardPaymentStudentBack.setOnClickListener {
                findNavController().navigateUp()
            }

            cardPaymentStudentProfile.setOnClickListener {

                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), Common.getCurrentStudent()!!, state = state)
            }

            spinnerPaymentStudentYear.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {

                        year = years[position] + "-" + "${years[position].toInt() + 1}"

                        observePaymentDataState()
                        viewModel.setPaymentStateEvent(
                            PaymentStudentStateEvent.Payment,
                            studentId  = Common.getCurrentStudent()!!.id,
                            year = year
                        )

                    }
                }
        }

    }

    private fun init() {
        paymentRecyclerAdapter = PaymentRecyclerAdapter(payments)
        initRecyclerAndAdapter()

        setYear(Common.getCurrentYear())

        val adapter = YearSpinnerAdapter(requireContext(), years)
        binding.spinnerPaymentStudentYear.adapter = adapter
    }

    private fun initRecyclerAndAdapter() {
        binding.apply {
            recyclerPaymentStudent.setHasFixedSize(true)
            recyclerPaymentStudent.adapter = paymentRecyclerAdapter
            recyclerPaymentStudent.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun observePaymentDataState() {
        viewModel.paymentsDataState.observe(viewLifecycleOwner) {
            when(it){
                is DataState.Loading -> {

                    binding.apply {
                        shimmerAnimationPaymentStudent.isVisible = true
                        recyclerPaymentStudent.isVisible = false
                        imgPaymentNoData.isVisible = false
                        txtPaymentNoData.isVisible = false
                    }
                }
                is DataState.Success -> {

                    payments = it.data as ArrayList<Payment>

                    if (payments.size != 0) {

                        paymentRecyclerAdapter.setData(payments)

                        binding.apply {
                            recyclerPaymentStudent.isVisible = true
                            imgPaymentNoData.isVisible = false
                            txtPaymentNoData.isVisible = false
                        }

                        paid = 0

                        observeCostDataState()
                        viewModel.setCostStateEvent(
                            PaymentStudentStateEvent.Cost,
                            studentId = Common.getCurrentStudent()!!.id,
                            groupId = Common.getCurrentStudent()!!.groupID
                        )

                        for (i in 0 until payments.size) {
                            paid += payments[i].pay
                        }

                    } else {
                        binding.apply {
                            imgPaymentNoData.isVisible = true
                            txtPaymentNoData.isVisible = true
                            txtPaymentTotalAmount.text = "0"
                            txtPaymentPaidAmount.text = "0"
                            txtPaymentRemainingAmount.text = "0"
                        }
                    }

                    viewModel.setPaymentStateEvent(PaymentStudentStateEvent.NonePayment)

                    binding.shimmerAnimationPaymentStudent.isVisible = false
                }
                is DataState.Failure -> {

                    binding.apply {
                        imgPaymentNoData.isVisible = true
                        txtPaymentNoData.isVisible = true
                        shimmerAnimationPaymentStudent.isVisible = false
                        recyclerPaymentStudent.isVisible = false
                        txtPaymentTotalAmount.text = "0"
                        txtPaymentPaidAmount.text = "0"
                        txtPaymentRemainingAmount.text = "0"
                    }

                    viewModel.setPaymentStateEvent(PaymentStudentStateEvent.NonePayment)
                }
                is DataState.ExceptionState -> {
                    viewModel.setPaymentStateEvent(PaymentStudentStateEvent.NonePayment)
                }
            }
        }
    }

    private fun observeCostDataState() {
        viewModel.costDataState.observe(viewLifecycleOwner) {
            when(it) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    binding.apply {
                        txtPaymentPaidAmount.text = paid.toString()
                        txtPaymentTotalAmount.text = it.data.toString()
                        txtPaymentRemainingAmount.text = it.data.minus(paid).toString()
                    }
                    viewModel.setCostStateEvent(PaymentStudentStateEvent.NoneCost)
                }
                is DataState.Failure -> {
                    viewModel.setCostStateEvent(PaymentStudentStateEvent.NoneCost)

                }
                is DataState.ExceptionState -> {
                    viewModel.setCostStateEvent(PaymentStudentStateEvent.NoneCost)

                }
            }
        }
    }

    private fun setYear(nowYear: String) {
        for (i in nowYear.toInt() downTo 2019) {
            years.add("$i")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}