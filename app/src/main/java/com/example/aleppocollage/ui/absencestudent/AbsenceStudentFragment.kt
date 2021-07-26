package com.example.aleppocollage.ui.absencestudent

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.MainActivity
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentAbsenceStudentBinding
import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.model.user.domain.Teacher
import com.example.aleppocollage.ui.chosedate.ChoseDateBottomSheet
import com.example.aleppocollage.ui.loading.LoadingDialog
import com.example.aleppocollage.util.Status
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Suppress("DEPRECATION")
class AbsenceStudentFragment : Fragment(R.layout.fragment_absence_student) {

    private lateinit var binding: FragmentAbsenceStudentBinding
    private var absenceViewModel: AbsenceStudentViewModel? = null

    companion object {
        val loadingDialog = LoadingDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAbsenceStudentBinding.bind(view)
        absenceViewModel = ViewModelProvider(this)[AbsenceStudentViewModel::class.java]
        binding.cardAbsenceStudentFragmentChoseDate.setOnClickListener {
            val choseDirections = ChoseDateBottomSheet()
            choseDirections.show(
                (activity as MainActivity).supportFragmentManager, "ChoseDateBottomSheet")
        }
        binding.btnAbsenceFragmentRecordTest.setOnClickListener {
            if (binding.txtAbsenceStudentFragmentChoseDate.text == resources.getString(R.string.enter_date_today)) {
                Toasty.warning(
                    activity as MainActivity,
                    resources.getString(R.string.please_enter_date),
                    Toast.LENGTH_SHORT,
                    true).show()
                return@setOnClickListener
            }
            val student = Paper.book().read<Student>("Student")
            getAbsenceStudent(
                student.id, binding.txtAbsenceStudentFragmentChoseDate.text.toString().trim())
        }
        val profileInfoStudent = Paper.book().read<Student>("Student")
        val profileInfoTeacher = Paper.book().read<Teacher>("Teacher")
        val typeUser = Paper.book().read<Int>("typeUser")
        binding.btnAbsenceStudentFragmentProfile.setOnClickListener {
            if (typeUser == 1) {
                (activity as MainActivity).showProfileInfo(1, profileInfoStudent, null)
            } else {
                (activity as MainActivity).showProfileInfo(2, null, profileInfoTeacher)
            }
        }

        binding.swipeRefreshAbsenceFragmentStudent.setOnRefreshListener {

        }
        binding.btnAbsenceStudentFragmentBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun getAbsenceStudent(studentID: Int, date: String) {
        absenceViewModel!!.getAbsenceStudent(studentID, date)
        absenceViewModel!!.absenceStudent.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    Log.d("state", "LOADING")
                    binding.swipeRefreshAbsenceFragmentStudent.isRefreshing = true
//                    binding.constraintAbsenceFragmentSession.visibility = View.VISIBLE
                  //  loadingDialog.show((activity as MainActivity).supportFragmentManager, "LoadingDialog")
                }
                Status.SUCCESS -> {
                    Log.d("state", "SUCCESS")
                    if (it._data?.session0 != null) {
                        binding.imgAbsenceFragmentChoseDate.visibility = View.GONE
                        binding.constraintAbsenceFragmentSession.visibility = View.VISIBLE

                    } else {
                        Toasty.error(activity as MainActivity, "لا يوجد بيانات لهذا التاريخ", Toast.LENGTH_SHORT, true).show()
                        binding.imgAbsenceFragmentChoseDate.visibility = View.GONE
                        binding.constraintAbsenceFragmentSession.visibility = View.VISIBLE
                    }
                    binding.swipeRefreshAbsenceFragmentStudent.isRefreshing = false
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
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}