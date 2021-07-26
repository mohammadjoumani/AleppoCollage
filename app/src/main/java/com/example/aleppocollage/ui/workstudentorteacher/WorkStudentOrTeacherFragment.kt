package com.example.aleppocollage.ui.workstudentorteacher

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.MainActivity
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.FragmentWorkStudentOrTeacherBinding
import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.model.user.domain.Teacher
import com.example.aleppocollage.ui.loading.LoadingDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.paperdb.Paper
import kotlinx.coroutines.delay

@Suppress("DEPRECATION")
class WorkStudentOrTeacherFragment() : Fragment() {

    private var _binding: FragmentWorkStudentOrTeacherBinding? = null
    private val binding get() = _binding!!

    companion object{
        val loadingDialog=LoadingDialog()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkStudentOrTeacherBinding.inflate(inflater, container, false)
        val view = binding.root

        if (Build.VERSION.SDK_INT >= 21) {
            val window = (activity as MainActivity).window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorWhite)
        }

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
            if(typeUser==1){
                (activity as MainActivity).showProfileInfo(1,profileInfoStudent,null)
            }else{
                (activity as MainActivity).showProfileInfo(2,null,profileInfoTeacher)
            }
        }

        binding.btnWorkFragmentNotification.setOnClickListener {
            val action =
                WorkStudentOrTeacherFragmentDirections.actionWorkStudentOrTeacherFragmentToNotificationFragment()
            findNavController().navigate(action)
        }

        binding.btnWorkFragmentBack.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                loadingDialog.show((activity as MainActivity).supportFragmentManager,"LoadingDialog")
                delay(3000)
                loadingDialog.dismiss()
                Paper.book().write("typeUser", 0)
                Paper.book().write("oneTime", true)
                val action =
                WorkStudentOrTeacherFragmentDirections.actionWorkStudentOrTeacherFragmentToRegisterFragment()
                findNavController().navigate(action)
                (activity as MainActivity).setHomeScreen(R.id.RegisterFragment)
            }
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