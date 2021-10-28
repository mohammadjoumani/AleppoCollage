package com.example.aleppocollage.ui.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.databinding.ItemRecyclerNotificationBinding
import com.example.aleppocollage.model.notification.domain.Notification

@Suppress("DEPRECATION")
class NotificationRecyclerAdapter(
    private var notifications: List<Notification>,
) : RecyclerView.Adapter<NotificationRecyclerAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemRecyclerNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        val notification = notifications[holder.layoutPosition]
        holder.bind(notification)

    }

    class NotificationViewHolder(
        val binding: ItemRecyclerNotificationBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(itemEntity: Notification) {

                binding.apply {
                    txtRecyclerNotificationTitle.text = itemEntity.title
                    txtRecyclerNotificationDate.text = itemEntity.date
                    txtRecyclerNotificationInfo.text = itemEntity.content
                }
            }
        }


    fun setData(notifications: List<Notification>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }
}