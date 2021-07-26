package com.example.aleppocollage.ui.notification

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.databinding.FragmentNotificationBinding
import com.example.aleppocollage.model.notification.domain.Notification
import com.example.aleppocollage.ui.notification.adapter.NotificationRecyclerAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.MainActivity
import com.example.aleppocollage.R
import com.example.aleppocollage.model.user.domain.Student
import com.example.aleppocollage.model.user.domain.Teacher
import com.example.aleppocollage.util.Status
import io.paperdb.Paper


@Suppress("DEPRECATION")
class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private lateinit var binding: FragmentNotificationBinding

    companion object {
        lateinit var notificationRecyclerAdapter: NotificationRecyclerAdapter
        lateinit var notifications: ArrayList<Notification>
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationBinding.bind(view)

        ///region Recycler Notification
        notifications = ArrayList()
        binding.recyclerNotificationFragment.setHasFixedSize(true)
        val layoutManagerMyEstate = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.recyclerNotificationFragment.layoutManager = layoutManagerMyEstate
        notificationRecyclerAdapter = NotificationRecyclerAdapter(notifications, context)
        binding.recyclerNotificationFragment.adapter = notificationRecyclerAdapter
        //endregion

        ///region get Notification
        val typeUser = Paper.book().read<Int>("typeUser")
        if (typeUser == 1) {
            val student = Paper.book().read<Student>("Student")
            getNotification(0, student.id, student.groupID, -1)
        } else if (typeUser == 2) {
            val teacher = Paper.book().read<Teacher>("Teacher")
            getNotification(1, -1, -1, teacher.id)
        }
        //endregion

        val profileInfoStudent = Paper.book().read<Student>("Student")
        val profileInfoTeacher = Paper.book().read<Teacher>("Teacher")
        binding.btnNotificationFragmentProfile.setOnClickListener {
            if(typeUser==1){
                (activity as MainActivity).showProfileInfo(1,profileInfoStudent,null)
            }else{
                (activity as MainActivity).showProfileInfo(2,null,profileInfoTeacher)
            }
        }

        binding.swipeRefreshNotificationFragmentStudent.setOnRefreshListener {

            if (typeUser == 1) {
                val student = Paper.book().read<Student>("Student")
                getNotification(0, student.id, student.groupID, -1)
            } else if (typeUser == 2) {
                val teacher = Paper.book().read<Teacher>("Teacher")
                getNotification(1, -1, -1, teacher.id)
            }
        }

        binding.btnNotificationFragmentBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getNotification(flag: Int, studentID: Int, groupID: Int, teacherId: Int) {
        val notificationViewModel = NotificationViewModel()
        notificationViewModel.getNotification(flag, studentID, groupID, teacherId)
        notificationViewModel.notifications.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    Log.d("state", "LOADING")
                    binding.swipeRefreshNotificationFragmentStudent.isRefreshing = true
                    binding.shimmerAnimationNotificationFragment.visibility = View.VISIBLE
                    binding.recyclerNotificationFragment.visibility = View.GONE
                    binding.imgNotificationFragmentNoData.visibility = View.GONE
                    binding.txtNotificationFragmentNoData.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    Log.d("state", "SUCCESS")
                    notifications = it._data!!
                    if (notifications.size != 0) {
                        notificationRecyclerAdapter.setData(notifications)
                        binding.recyclerNotificationFragment.visibility = View.VISIBLE
                        binding.imgNotificationFragmentNoData.visibility = View.GONE
                        binding.txtNotificationFragmentNoData.visibility = View.GONE
                    } else {
                        binding.imgNotificationFragmentNoData.visibility = View.VISIBLE
                        binding.txtNotificationFragmentNoData.visibility = View.VISIBLE
                    }
                    binding.shimmerAnimationNotificationFragment.visibility = View.GONE
                    binding.swipeRefreshNotificationFragmentStudent.isRefreshing = false


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
}