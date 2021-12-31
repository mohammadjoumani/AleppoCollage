package com.example.aleppocollage.ui.payment.teacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentPaymentTeacherBinding
import com.example.aleppocollage.model.payment.domain.Payment
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.mark.student.adapter.YearSpinnerAdapter
import com.example.aleppocollage.ui.payment.adapter.PaymentRecyclerAdapter
import com.example.aleppocollage.ui.payment.student.PaymentStudentStateEvent
import com.example.aleppocollage.ui.util.loading.LoadingViewModel
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class PaymentTeacherFragment : Fragment(R.layout.fragment_payment_teacher) {

    ///region Variables

    private var years: ArrayList<String> = ArrayList()
    private var payments: ArrayList<Payment> = ArrayList()
    private var year = -1

    private var paid = 0

    //endregion

    ///region ViewModel & Binding

    private val viewModel: PaymentTeacherViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentPaymentTeacherBinding? = null
    private val binding get() = _binding!!

    ///endregion

    private lateinit var paymentRecyclerAdapter: PaymentRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPaymentTeacherBinding.bind(view)

        init()

        binding.apply {

            swipeRefreshPaymentTeacher.setColorSchemeColors(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

            swipeRefreshPaymentTeacher.setOnRefreshListener {
                observePaymentDataState()
                viewModel.setPaymentStateEvent(
                    PaymentTeacherStateEvent.Payment,
                    teacherId = Common.getCurrentTeacher()!!.id,
                    year = "$year-${year + 1}"
                )
            }

            cardPaymentTeacherBack.setOnClickListener {
                findNavController().navigateUp()
            }

            cardPaymentTeacherProfile.setOnClickListener {
                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = state)
            }

            binding.spinnerPaymentTeacherYear.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {

                        year = years[position].toInt()

                        observePaymentDataState()
                        viewModel.setPaymentStateEvent(
                            PaymentTeacherStateEvent.Payment,
                            teacherId = Common.getCurrentTeacher()!!.id,
                            year = "$year-${year + 1}"
                        )

                    }
                }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (sharedViewModel.showProfileInfo.value!!.state) {
                    sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(), teacher = Common.getCurrentTeacher()!!, state = false)
                } else{
                    findNavController().navigateUp()
                }
            }
        })

    }

    private fun init() {

        paymentRecyclerAdapter = PaymentRecyclerAdapter(payments)
        initRecyclerAndAdapter()

        setYear(Common.getCurrentYear())

        val adapter = YearSpinnerAdapter(requireContext(), years)
        binding.spinnerPaymentTeacherYear.adapter = adapter
    }

    private fun initRecyclerAndAdapter() {
        binding.apply {
            recyclerPaymentTeacher.setHasFixedSize(true)
            recyclerPaymentTeacher.adapter = paymentRecyclerAdapter
            recyclerPaymentTeacher.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun observePaymentDataState() {
        viewModel.paymentsDataState.observe(viewLifecycleOwner) {
            binding.apply {
                when(it){
                    is DataState.Loading -> {
                        shimmerAnimationPaymentTeacher.isVisible = true
                        swipeRefreshPaymentTeacher.isRefreshing = true
                        recyclerPaymentTeacher.isVisible = false
                        imgPaymentNoData.isVisible = false
                        txtPaymentNoData.isVisible = false
                    }
                    is DataState.Success -> {
                        payments = it.data as ArrayList<Payment>
                        shimmerAnimationPaymentTeacher.isVisible = false
                        swipeRefreshPaymentTeacher.isRefreshing = false
                        if (payments.size != 0) {
                                paymentRecyclerAdapter.setData(payments)

                                recyclerPaymentTeacher.isVisible = true
                                imgPaymentNoData.isVisible = false
                                txtPaymentNoData.isVisible = false

                                paid = 0

                                observeCostDataState()
                                viewModel.setCostStateEvent(
                                    PaymentTeacherStateEvent.Cost,
                                    teacherId = Common.getCurrentTeacher()!!.id,
                                    groupId = 0, year = "$year-${year + 1}"
                                )

                                for (i in 0 until payments.size) {
                                    paid += payments[i].pay
                                }

                            } else {
                                imgPaymentNoData.isVisible = true
                                txtPaymentNoData.isVisible = true
                                txtPaymentTotalAmount.text = "0"
                                txtPaymentPaidAmount.text = "0"
                                txtPaymentRemainingAmount.text = "0"
                            }

                        viewModel.setPaymentStateEvent(PaymentTeacherStateEvent.NonePayment)
                    }
                    is DataState.Failure -> {
                        imgPaymentNoData.isVisible = true
                        txtPaymentNoData.isVisible = true
                        shimmerAnimationPaymentTeacher.isVisible = false
                        swipeRefreshPaymentTeacher.isRefreshing = false
                        recyclerPaymentTeacher.isVisible = false
                        txtPaymentTotalAmount.text = "0"
                        txtPaymentPaidAmount.text = "0"
                        txtPaymentRemainingAmount.text = "0"

                        Common.showToast(requireActivity(), getString(R.string.strNotFoundData),"error")
                        viewModel.setPaymentStateEvent(PaymentTeacherStateEvent.NonePayment)
                    }
                    is DataState.ExceptionState -> {
                        imgPaymentNoData.isVisible = false
                        txtPaymentNoData.isVisible = false
                        shimmerAnimationPaymentTeacher.isVisible = false
                        swipeRefreshPaymentTeacher.isRefreshing = false
                        recyclerPaymentTeacher.isVisible = false
                        txtPaymentTotalAmount.text = "0"
                        txtPaymentPaidAmount.text = "0"
                        txtPaymentRemainingAmount.text = "0"

                        Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                        viewModel.setPaymentStateEvent(PaymentTeacherStateEvent.NonePayment)
                    }
                    is DataState.Connection -> {
                        imgPaymentNoData.isVisible = false
                        txtPaymentNoData.isVisible = false
                        shimmerAnimationPaymentTeacher.isVisible = false
                        swipeRefreshPaymentTeacher.isRefreshing = false
                        recyclerPaymentTeacher.isVisible = false
                        txtPaymentTotalAmount.text = "0"
                        txtPaymentPaidAmount.text = "0"
                        txtPaymentRemainingAmount.text = "0"

                        Common.showSnackBar(requireContext(), binding.root, getString(R.string.strThereIsProblem))
                        viewModel.setPaymentStateEvent(PaymentTeacherStateEvent.NonePayment)
                    }
                }
            }
        }
    }

    private fun observeCostDataState() {
        viewModel.costDataState.observe(viewLifecycleOwner) {
            binding.apply {
                when(it) {
                    is DataState.Loading -> {

                    }
                    is DataState.Success -> {
                        txtPaymentPaidAmount.text = paid.toString()
                        txtPaymentTotalAmount.text = it.data.toString()
                        txtPaymentRemainingAmount.text = it.data?.minus(paid).toString()

                        viewModel.setCostStateEvent(PaymentTeacherStateEvent.NoneCost)
                    }
                    is DataState.Failure -> {
                        viewModel.setCostStateEvent(PaymentTeacherStateEvent.NoneCost)

                    }
                    is DataState.ExceptionState -> {
                        viewModel.setCostStateEvent(PaymentTeacherStateEvent.NoneCost)

                    }
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
