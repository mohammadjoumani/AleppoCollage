package com.example.aleppocollage.ui.payment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.R
import com.example.aleppocollage.databinding.ItemRecyclerPaymentBinding
import com.example.aleppocollage.model.mark.domain.Mark
import com.example.aleppocollage.model.payment.domain.Payment

class PaymentRecyclerAdapter(
    private var payments: List<Payment>,
    private val context: Context?
) : RecyclerView.Adapter<PaymentRecyclerAdapter.PaymentViewHolder>() {

    inner class PaymentViewHolder(val binding: ItemRecyclerPaymentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val view = LayoutInflater.from(parent.context)
        return PaymentViewHolder(ItemRecyclerPaymentBinding.inflate(view, parent, false))
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.binding.txtRecyclerPaymentNumber.text = payments[holder.adapterPosition].payNo.toString()
        holder.binding.txtRecyclerPaymentDate.text = payments[holder.adapterPosition].date
        holder.binding.txtRecyclerPaymentPay.text = payments[holder.adapterPosition].pay.toString()
        holder.binding.txtRecyclerPaymentDateMonth.text = payments[holder.adapterPosition].dateForMonth

    }

    fun setData(payments: List<Payment>) {
        this.payments = payments
        notifyDataSetChanged()
    }
}