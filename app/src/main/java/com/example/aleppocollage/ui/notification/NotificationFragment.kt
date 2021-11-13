package com.example.aleppocollage.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.databinding.FragmentNotificationBinding
import com.example.aleppocollage.model.notification.domain.Notification
import com.example.aleppocollage.ui.notification.adapter.NotificationRecyclerAdapter
import androidx.navigation.fragment.findNavController
import com.example.aleppocollage.R
import com.example.aleppocollage.ui.main.model.ProfileInfo
import com.example.aleppocollage.ui.util.loading.LoadingViewModel
import com.example.aleppocollage.ui.util.sharedviewmodel.SharedViewModel
import com.example.aleppocollage.util.Common
import com.example.aleppocollage.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import io.paperdb.Paper

@AndroidEntryPoint
class NotificationFragment : Fragment(R.layout.fragment_notification) {

    ///region Variables

    private var notifications: ArrayList<Notification> = ArrayList()

    //endregion

    ///region ViewModel & Binding

    private val viewModel: NotificationViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    ///endregion

    private lateinit var notificationRecyclerAdapter: NotificationRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotificationBinding.bind(view)

        notificationRecyclerAdapter = NotificationRecyclerAdapter(notifications)
        initRecyclerAndAdapter()

        ///region get Notification

        val userType = Common.getCurrentTypeUser()

        if (userType == "Student") {
            val student = Common.getCurrentStudent()
            observeNotificationDataState()
            viewModel.setStateEvent(NotificationStateEvent.Notification, 0, student!!.id, student!!.groupID, -1)
        } else if (userType == "Teacher") {
            val teacher = Common.getCurrentTeacher()
            observeNotificationDataState()
            viewModel.setStateEvent(NotificationStateEvent.Notification, 1, -1, -1, teacher!!.id)
        }

        //endregion

        binding.apply {

            cardNotificationProfile.setOnClickListener {

                val state = !(sharedViewModel.showProfileInfo.value!!.state)
                sharedViewModel.showProfileInfo.value = ProfileInfo(userType = Common.getCurrentTypeUser(),Common.getCurrentStudent()!!, state = state)

            }

            swipeRefreshNotification.setColorSchemeColors(getColor(requireContext(), R.color.colorPrimary))

            swipeRefreshNotification.setOnRefreshListener {

                if (userType == "Student") {
                    val student = Common.getCurrentStudent()
                    observeNotificationDataState()
                    viewModel.setStateEvent(NotificationStateEvent.Notification, 0, student!!.id, student!!.groupID, -1)
                } else if (userType == "Teacher") {
                    val teacher = Common.getCurrentTeacher()
                    observeNotificationDataState()
                    viewModel.setStateEvent(NotificationStateEvent.Notification, 1, -1, -1, teacher!!.id)
                }

            }

            binding.cardNotificationBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }

    }

    private fun initRecyclerAndAdapter() {
        binding.apply {
            recyclerNotification.setHasFixedSize(true)
            recyclerNotification.adapter = notificationRecyclerAdapter
            recyclerNotification.layoutManager =
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun observeNotificationDataState() {
        viewModel.notificationsDataState.observe(viewLifecycleOwner) {
            when(it) {
                is DataState.Loading -> {
                    binding.apply {
                        swipeRefreshNotification.isRefreshing = true
                        shimmerAnimationNotification.isVisible = true
                        recyclerNotification.isVisible = false
                        imgNotificationNoData.isVisible = false
                        txtNotificationNoData.isVisible = false
                    }
                }
                is DataState.Success -> {
                    notifications = it.data as ArrayList<Notification>

                    if (notifications.size != 0) {
                        notificationRecyclerAdapter.setData(notifications)
                        binding.apply {
                            recyclerNotification.isVisible = true
                            imgNotificationNoData.isVisible = false
                            txtNotificationNoData.isVisible = false
                        }
                    } else {
                        binding.apply {
                            recyclerNotification.isVisible = false
                            imgNotificationNoData.isVisible = true
                            txtNotificationNoData.isVisible = true
                        }
                    }
                    binding.apply {
                        shimmerAnimationNotification.isVisible = false
                        swipeRefreshNotification.isRefreshing = false
                    }
                    viewModel.setStateEvent(NotificationStateEvent.None)
                }
                is DataState.Failure -> {
                    Common.showSnackBar(requireContext(), binding.root, it.message)
                    viewModel.setStateEvent(NotificationStateEvent.None)

                }
                is DataState.ExceptionState -> {
                    Common.showSnackBar(requireContext(), binding.root, "${it}")
                    viewModel.setStateEvent(NotificationStateEvent.None)

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}