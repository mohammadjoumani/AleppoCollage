package com.example.aleppocollage.ui.payment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import android.widget.AdapterView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentPaymentBinding
import com.example.aleppocollage.model.payment.domain.Payment
import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.model.user.domain.Teacher
import com.example.aleppocollage.ui.markStudent.adapter.YearSpinnerAdapter
import com.example.aleppocollage.ui.payment.adapter.PaymentRecyclerAdapter
import com.example.aleppocollage.util.Status
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private lateinit var binding: FragmentPaymentBinding
    private var years: ArrayList<String>? = null

    private var paid = 0

    companion object {
        lateinit var paymentRecyclerAdapter: PaymentRecyclerAdapter
        lateinit var payments: ArrayList<Payment>
        private lateinit var year: String
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPaymentBinding.bind(view)

        ///region recycler payments
        payments = ArrayList()
        binding.recyclerPaymentFragment.setHasFixedSize(true)
        val layoutManagerMyEstate = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.recyclerPaymentFragment.layoutManager = layoutManagerMyEstate
        paymentRecyclerAdapter = PaymentRecyclerAdapter(payments, context)
        binding.recyclerPaymentFragment.adapter = paymentRecyclerAdapter
        //endregion

        ///region spinner Years
        val date: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy", Locale.getDefault())
        val formattedDate: String = df.format(date)
        years = ArrayList()
        setYear(formattedDate.toInt())

        val adapter = activity?.applicationContext?.let {
            YearSpinnerAdapter(it, years!!)
        }
        binding.spinnerPaymentFragmentYear.adapter = adapter

        binding.spinnerPaymentFragmentYear.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val typeUser = Paper.book().read<Int>("typeUser")
                    if (typeUser == 1) {
                        val student = Paper.book().read<Student>("Student")
                        val year = adapterView?.getItemAtPosition(position).toString()
                        getPay(student.id, "$year-${year.toInt() + 1}")
                    } else if (typeUser == 2) {
                        val teacher = Paper.book().read<Teacher>("Teacher")
                        year=adapterView?.getItemAtPosition(position).toString()
                        val year = adapterView?.getItemAtPosition(position).toString()
                        getPay(teacher.id, "$year-${year.toInt() + 1}")
                    }
                }
            }
        //endregion
    }

    private fun getPay(id: Int, studyYear: String) {
        val paymentViewModel = PaymentViewModel()
        val typeUser = Paper.book().read<Int>("typeUser")
        if (typeUser == 1) {
            paymentViewModel.getPayStudent(id, studyYear)
        } else if (typeUser == 2) {
            paymentViewModel.getPayTeacher(id, studyYear)
        }
        paymentViewModel.payStudent.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    binding.shimmerAnimationMarkFragment.visibility = View.VISIBLE
                    binding.recyclerPaymentFragment.visibility = View.GONE
                    binding.imgPaymentFragmentNoData.visibility = View.GONE
                    binding.txtPaymentFragmentNoData.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    payments = it._data!!
                    if (payments.size != 0) {
                        paid=0
                        val typeUser = Paper.book().read<Int>("typeUser")
                        if (typeUser == 1) {
                            val student = Paper.book().read<Student>("Student")
                            getCost(student.id,student.groupID,"")
                        } else if (typeUser == 2) {
                            val teacher = Paper.book().read<Teacher>("Teacher")
                            getCost(teacher.id, 0,"$year-${year.toInt() + 1}")
                        }
                        paymentRecyclerAdapter.setData(payments)
                        binding.recyclerPaymentFragment.visibility = View.VISIBLE
                        binding.imgPaymentFragmentNoData.visibility = View.GONE
                        binding.txtPaymentFragmentNoData.visibility = View.GONE
                        for (i in 0 until payments.size){
                            paid+= payments[i].pay
                        }
                        Log.d("paid","$paid")
                    } else {
                        binding.imgPaymentFragmentNoData.visibility = View.VISIBLE
                        binding.txtPaymentFragmentNoData.visibility = View.VISIBLE
                        binding.txtPaymentFragmentTotalAmount.text="0"
                        binding.txtPaymentFragmentPaidAmount.text="0"
                        binding.txtPaymentFragmentRemainingAmount.text="0"
                    }
                    binding.shimmerAnimationMarkFragment.visibility = View.GONE
                }

                Status.FAILURE -> {
                    Log.d("stateMark", "Failure")
                }

                Status.ERROR -> {
                    Log.d("stateMark", "ERROR")
                }
            }
        })
    }

    private fun getCost(id:Int,groupID:Int,studyYear:String){
        val paymentViewModel = PaymentViewModel()
        val typeUser = Paper.book().read<Int>("typeUser")
        if (typeUser == 1) {
            paymentViewModel.getCostStudent(id,groupID)
        } else if (typeUser == 2) {
            paymentViewModel.getCostTeacher(id,groupID,studyYear)
        }
        paymentViewModel.getCostStudent(id,groupID)
        paymentViewModel.cost.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.LOADING->{

                }
                Status.FAILURE->{

                }
                Status.SUCCESS->{
                    binding.txtPaymentFragmentPaidAmount.text=paid.toString()
                    binding.txtPaymentFragmentTotalAmount.text=it._data.toString()
                    val remaining= it._data?.minus(paid)
                    binding.txtPaymentFragmentRemainingAmount.text= remaining.toString()
                }
                Status.ERROR->{

                }
            }
        })
    }

    private fun setYear(nowYear: Int) {
        for (i in 2019..nowYear) {
            Log.d("year1", "$i")
            years?.add("$i")
        }
    }

}