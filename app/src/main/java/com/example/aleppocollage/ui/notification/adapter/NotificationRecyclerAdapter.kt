package com.example.aleppocollage.ui.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.ItemRecyclerNotificationBinding
import com.example.aleppocollage.model.notification.domain.Notification

class NotificationRecyclerAdapter(
    private var notifications: List<Notification>,
    private val context: Context?
) : RecyclerView.Adapter<NotificationRecyclerAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(val binding: ItemRecyclerNotificationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
        return NotificationViewHolder(ItemRecyclerNotificationBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.binding.txtRecyclerNotificationTitle.text=notifications[holder.adapterPosition].title
        holder.binding.txtRecyclerNotificationDate.text=notifications[holder.adapterPosition].date
        holder.binding.txtRecyclerNotificationInfo.text=notifications[holder.adapterPosition].content
    }

    fun setData(notifications: List<Notification>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }
}