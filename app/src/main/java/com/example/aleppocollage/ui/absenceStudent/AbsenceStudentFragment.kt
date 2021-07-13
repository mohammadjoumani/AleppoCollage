package com.example.aleppocollage.ui.absenceStudent

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.aleppocollage.MainActivity
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentAbsenceStudentBinding
import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.ui.choseDate.ChoseDateBottomSheet
import com.example.aleppocollage.util.Status
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Suppress("DEPRECATION")
class AbsenceStudentFragment : Fragment(R.layout.fragment_absence_student) {

    private lateinit var binding: FragmentAbsenceStudentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAbsenceStudentBinding.bind(view)

        binding.cardAbsenceStudentFragmentChoseDate.setOnClickListener {
            val choseDirections = ChoseDateBottomSheet()
            choseDirections.show((activity as MainActivity).supportFragmentManager, "ChoseDateBottomSheet")
        }
        binding.btnAbsenceFragmentRecordTest.setOnClickListener {
            if (binding.txtAbsenceStudentFragmentChoseDate.text == resources.getString(R.string.enter_date_today)) {
                Toasty.warning(activity as MainActivity, resources.getString(R.string.please_enter_date), Toast.LENGTH_SHORT, true).show()
                return@setOnClickListener
            }
            val student = Paper.book().read<Student>("Student")
            getAbsenceStudent(
                student.id,
                binding.txtAbsenceStudentFragmentChoseDate.text.toString().trim()
            )
        }

        binding.swipeRefreshAbsenceFragmentStudent.setOnRefreshListener {

        }

    }

    private fun getAbsenceStudent(studentID: Int, date: String) {
        val absenceViewModel = AbsenceStudentViewModel()
        absenceViewModel.getAbsenceStudent(studentID, date)
        absenceViewModel.absenceStudent.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    Log.d("state", "LOADING")
                    binding.swipeRefreshAbsenceFragmentStudent.isRefreshing = true
                    binding.imgAbsenceFragmentChoseDate.visibility = View.GONE
                    binding.constraintAbsenceFragmentSession.visibility = View.VISIBLE
                    binding.swipeRefreshAbsenceFragmentStudent.isRefreshing = false
                }
                Status.SUCCESS -> {
                    Log.d("state", "SUCCESS")

                }

                Status.FAILURE -> {
                    Log.d("state", "FAILURE")

                }

                Status.ERROR -> {
                    Log.d("state", "ERROR")
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onResultReceived(result: String?) {
        binding.txtAbsenceStudentFragmentChoseDate.text = "$result-2020"
        binding.txtAbsenceStudentFragmentChoseDate.setTextColor(resources.getColor(R.color.colorPurple))
        binding.txtAbsenceStudentFragmentChoseDate.textSize = 20F
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}