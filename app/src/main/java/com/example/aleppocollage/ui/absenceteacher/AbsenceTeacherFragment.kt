package com.example.aleppocollage.ui.absenceteacher

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.MainActivity
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentAbsenceTeacherBinding
import com.example.aleppocollage.listener.AbsenceListener
import com.example.aleppocollage.model.absence.domain.Absence
import com.example.aleppocollage.model.test.domain.DeservedGroup
import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.model.user.domain.Teacher
import com.example.aleppocollage.ui.absencestudent.AbsenceStudentViewModel
import com.example.aleppocollage.ui.absenceteacher.adapter.AbsenceTeacherRecyclerAdapter
import com.example.aleppocollage.ui.chosedate.ChoseDateBottomSheet
import com.example.aleppocollage.ui.loading.LoadingDialog
import com.example.aleppocollage.ui.markstudent.adapter.YearSpinnerAdapter
import com.example.aleppocollage.ui.testteacher.adapter.DeservedGroupsSpinnerAdapter
import com.example.aleppocollage.util.Status
import es.dmoral.toasty.Toasty
import io.paperdb.Paper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION", "UNREACHABLE_CODE")
class AbsenceTeacherFragment : Fragment(R.layout.fragment_absence_teacher), AbsenceListener {

    private lateinit var binding: FragmentAbsenceTeacherBinding
    private val absenceTeacherViewModel by viewModels<AbsenceTeacherViewModel>()

    private var years: ArrayList<String>? = null
    private var groupID: Int = 0

    companion object {
        private lateinit var absenceTeacherRecyclerAdapter: AbsenceTeacherRecyclerAdapter
        private lateinit var absences: ArrayList<Absence>
        private lateinit var deservedGroups: ArrayList<DeservedGroup>
        val loadingDialog = LoadingDialog()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAbsenceTeacherBinding.bind(view)

        ///region recycler absence
        absences = ArrayList()
        binding.recyclerAbsenceTeacher.setHasFixedSize(true)
        val layoutManagerMyEstate = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.recyclerAbsenceTeacher.layoutManager = layoutManagerMyEstate
        absenceTeacherRecyclerAdapter = AbsenceTeacherRecyclerAdapter(absences, context, this)
        binding.recyclerAbsenceTeacher.adapter = absenceTeacherRecyclerAdapter
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
        binding.spinnerAbsenceTeacherFragmentYear.adapter = adapter

        binding.spinnerAbsenceTeacherFragmentYear.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val year = adapterView?.getItemAtPosition(position).toString()
                    val teacher = Paper.book().read<Teacher>("Teacher")
                    getDeservedGroup("$year-${year.toInt() + 1}", teacher.id)
                }
            }
        //endregion

        ///region Spinner Department
        binding.spinnerAbsenceTeacherFragmentDepartment.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    groupID = deservedGroups[position].id
                    Log.d("groupID", "$groupID")
                }
            }

        binding.cardAbsenceTeacherFragmentChoseDate.setOnClickListener {
            val choseDirections = ChoseDateBottomSheet()
            choseDirections.show(
                (activity as MainActivity).supportFragmentManager, "ChoseDateBottomSheet")
        }
        //endregion

        binding.btnAbsenceTeacherFragmentRecordTest.setOnClickListener {
            val date = binding.txtAbsenceTeacherFragmentChoseDate.text.toString()
            if (date == resources.getString(R.string.enterDateToday)) {
                Toasty.warning(
                    activity as MainActivity,
                    resources.getString(R.string.pleaseEnterDate),
                    Toast.LENGTH_SHORT,
                    true).show()
                return@setOnClickListener
            }
            getGroupAbsenceSelect(groupID, date)
        }

        binding.swipeRefreshAbsenceFragmentTeacher.setOnRefreshListener {

        }

        val profileInfoStudent = Paper.book().read<Student>("Student")
        val profileInfoTeacher = Paper.book().read<Teacher>("Teacher")
        val typeUser = Paper.book().read<Int>("typeUser")
        binding.btnAbsenceTeacherFragmentProfile.setOnClickListener {
            if (typeUser == 1) {
                (activity as MainActivity).showProfileInfo(1, profileInfoStudent, null)
            } else {
                (activity as MainActivity).showProfileInfo(2, null, profileInfoTeacher)
            }
        }

        binding.btnAbsenceTeacherFragmentBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    private fun getGroupAbsenceSelect(groupID: Int, date: String) {
        absenceTeacherViewModel.getGroupAbsenceSelect(groupID, date)
        absenceTeacherViewModel.absenceGroup?.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    Log.d("state1", "LOADING")
                    loadingDialog.show(
                        (activity as MainActivity).supportFragmentManager, "LoadingDialog")
                }
                Status.SUCCESS -> {
                    Log.d("state1", "SUCCESS")
                    absences = it._data!!
                    Log.d("date", "${absences[0].session9}")
                    if (absences.size != 0 && absences[0].session0 == null) {
                        binding.btnAbsenceTeacherFragmentSave.visibility=View.VISIBLE
                        binding.swipeRefreshAbsenceFragmentTeacher.visibility = View.GONE
                        binding.relativeFragmentTeacher.visibility = View.VISIBLE
                        binding.recyclerAbsenceTeacher.visibility = View.VISIBLE
                        binding.linearAbsenceTeacherInformation.visibility = View.GONE
                        absenceTeacherRecyclerAdapter.setData(absences)
                    } else if (absences.size != 0 && absences[0].session0 != null) {
                        binding.btnAbsenceTeacherFragmentSave.visibility=View.GONE
                        binding.swipeRefreshAbsenceFragmentTeacher.visibility = View.VISIBLE
                        binding.relativeFragmentTeacher.visibility = View.GONE
                        binding.recyclerAbsenceTeacher.visibility = View.GONE
                        binding.linearAbsenceTeacherInformation.visibility = View.VISIBLE
                    }
                    loadingDialog.dismiss()
                }
                Status.FAILURE -> {
                    Log.d("state1", "FAILURE")
                }
                Status.ERROR -> {
                    Log.d("state1", "ERROR")
                }
            }
        })
    }

    private fun getDeservedGroup(studyYear: String, teacherID: Int) {
        val absenceTeacherViewModel = AbsenceTeacherViewModel()
        absenceTeacherViewModel.getDeservedGroup(studyYear, teacherID)
        absenceTeacherViewModel.deservedGroup?.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    Log.d("state", "LOADING")
                    loadingDialog.show(
                        (activity as MainActivity).supportFragmentManager, "LoadingDialog")
                    binding.spinnerAbsenceTeacherFragmentDepartment.isEnabled = false
                }
                Status.SUCCESS -> {
                    Log.d("state", "SUCCESS")
                    loadingDialog.dismiss()
                    deservedGroups = it._data!!
                    val adapter = activity?.applicationContext?.let {
                        DeservedGroupsSpinnerAdapter(activity as MainActivity, deservedGroups)
                    }
                    binding.spinnerAbsenceTeacherFragmentDepartment.adapter = adapter
                    binding.spinnerAbsenceTeacherFragmentDepartment.isEnabled = true
                    if (deservedGroups.size == 0) {
                        binding.spinnerAbsenceTeacherFragmentDepartment.isEnabled = false
                        Toasty.info(
                            activity as MainActivity,
                            resources.getString(R.string.noGroups),
                            Toast.LENGTH_SHORT).show()
                    }
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
        binding.txtAbsenceTeacherFragmentChoseDate.text = "$result-2019"
        binding.txtAbsenceTeacherFragmentChoseDate.setTextColor(resources.getColor(R.color.colorPurple))
        binding.txtAbsenceTeacherFragmentChoseDate.textSize = 20F
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun setYear(nowYear: Int) {
        for (i in 2019..nowYear) {
            Log.d("year1", "$i")
            years?.add("$i")
        }
    }

    override fun onAbsenceChange(absences: List<Absence?>?) {
        Log.d("absencesL1", "${absences?.get(1)?.note}")
        Log.d("absencesL2", "${absences?.get(1)?.session0}")
        Log.d("absencesL3", "${absences?.get(1)?.session1}")
        Log.d("absencesL4", "${absences?.get(1)?.session2}")
    }

}