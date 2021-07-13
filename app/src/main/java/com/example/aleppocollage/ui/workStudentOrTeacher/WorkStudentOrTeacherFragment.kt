package com.example.aleppocollage.ui.workStudentOrTeacher

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.MainActivity
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentWorkStudentOrTeacherBinding
import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.model.user.domain.Teacher
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.paperdb.Paper

@Suppress("DEPRECATION")
class WorkStudentOrTeacherFragment() : Fragment() {

    private var _binding: FragmentWorkStudentOrTeacherBinding? = null
    private val binding get() = _binding!!

    private var showProfileInfo = 0

    private lateinit var myContext: FragmentActivity

    override fun onAttach(activity: Activity) {
        myContext = activity as FragmentActivity
        super.onAttach(activity)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkStudentOrTeacherBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.btnWorkFragmentBack.setOnClickListener {
            val cm: ConnectivityManager =
                (activity as MainActivity).getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo: NetworkInfo? = cm.activeNetworkInfo
            val nc: NetworkCapabilities? =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cm.getNetworkCapabilities(cm.activeNetwork)
                } else {
                    TODO("VERSION.SDK_INT < M")
                }
            val downSpeed = nc?.linkDownstreamBandwidthKbps
            val upSpeed = nc?.linkUpstreamBandwidthKbps
            if (downSpeed != null) {
                Log.d("downSpeed", "${downSpeed / 1000}")
            }
            if (upSpeed != null) {
                Log.d("upSpeed", "${upSpeed / 1000}")
            }
        }

        if (Build.VERSION.SDK_INT >= 21) {
            val window = myContext.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorWhite)
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (activity as MainActivity).finish()
                }
            }

        val oneTime = Paper.book().read<Boolean>("oneTime", true)
//        (activity as MainActivity?)!!.setHomeScreen(R.id.workStudentOrTeacherFragment)

        val typeUser = Paper.book().read<Int>("typeUser")
        if (typeUser == 1) {
            binding.imgWorkFragmentBackGround.setImageResource(R.drawable.ic_student_pana)
            binding.navWorkFragmentMenu.inflateMenu(R.menu.bottom_nav_menu_student)
            binding.navWorkFragmentMenu.setOnNavigationItemSelectedListener(
                mOnNavigationStudentItemSelectedListener
            )
        } else if (typeUser == 2) {
            binding.imgWorkFragmentBackGround.setImageResource(R.drawable.ic_professor_pana)
            binding.navWorkFragmentMenu.inflateMenu(R.menu.bottom_nav_menu_teacher)
            binding.navWorkFragmentMenu.setOnNavigationItemSelectedListener(
                mOnNavigationTeacherItemSelectedListener
            )
        }

        val profileInfoStudent = Paper.book().read<Student>("Student")
        val profileInfoTeacher = Paper.book().read<Teacher>("Teacher")

        binding.btnWorkFragmentProfile.setOnClickListener {
            if (showProfileInfo == 0) {
                binding.LinearWorkStudentOrTeacherInfo.visibility = View.VISIBLE
                if (typeUser == 1) {
                    binding.txtWorkStudentOrTeacherName.text = profileInfoStudent.name
                    binding.txtWorkStudentOrTeacherGroup.text = profileInfoStudent.groupLevel
                } else if (typeUser == 2) {
                    binding.txtWorkStudentOrTeacherName.text = profileInfoTeacher.name
                }

                showProfileInfo = 1
            } else if (showProfileInfo == 1) {
                binding.LinearWorkStudentOrTeacherInfo.visibility = View.GONE
                showProfileInfo = 0
            }
        }

        binding.btnWorkFragmentNotification.setOnClickListener {
            val action =
                WorkStudentOrTeacherFragmentDirections.actionWorkStudentOrTeacherFragmentToNotificationFragment()
            findNavController().navigate(action)
        }
        return view
    }


    //for student
    private val mOnNavigationStudentItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigationAbsence -> {
                    val action =
                        WorkStudentOrTeacherFragmentDirections.actionWorkStudentOrTeacherFragmentToAbsenceStudentFragment()
                    findNavController().navigate(action)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationMark -> {
                    val action =
                        WorkStudentOrTeacherFragmentDirections.actionWorkStudentOrTeacherFragmentToMarkFragment()
                    findNavController().navigate(action)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationPayment -> {
                    val action =
                        WorkStudentOrTeacherFragmentDirections.actionWorkStudentOrTeacherFragmentToPaymentFragment()
                    findNavController().navigate(action)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    //for teacher
    private val mOnNavigationTeacherItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigationAbsence -> {
                    val teacher = Paper.book().read<Teacher>("Teacher")
                    if (TextUtils.substring(teacher.name, 0, 6) == "الموجه") {
                        val action =
                            WorkStudentOrTeacherFragmentDirections.actionWorkStudentOrTeacherFragmenToAbsenceTeacherFragment()
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(activity, "هذه ليست من صلاحيات الأستاذ", Toast.LENGTH_SHORT)
                            .show()
                    }

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationTest -> {
                    val action =
                        WorkStudentOrTeacherFragmentDirections.actionWorkStudentOrTeacherFragmentToTestFragment()
                    findNavController().navigate(action)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigationPayment -> {
                    val action =
                        WorkStudentOrTeacherFragmentDirections.actionWorkStudentOrTeacherFragmentToPaymentFragment()
                    findNavController().navigate(action)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}