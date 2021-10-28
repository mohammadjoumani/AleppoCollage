package com.example.aleppocollage.ui.payment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.aleppocollage.databinding.ItemRecyclerPaymentBinding
import com.example.aleppocollage.model.payment.domain.Payment

class PaymentRecyclerAdapter(
    private var payments: List<Payment>,
) : RecyclerView.Adapter<PaymentRecyclerAdapter.PaymentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val binding =
            ItemRecyclerPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {

       val payment = payments[holder.layoutPosition]
        holder.bind(payment)
    }

     class PaymentViewHolder(
         val binding: ItemRecyclerPaymentBinding
         ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(itemEntity: Payment) {
                binding.apply {
                    txtRecyclerPaymentNumber.text = itemEntity.payNo.toString()
                    txtRecyclerPaymentDate.text = itemEntity.date
                    txtRecyclerPaymentPay.text = itemEntity.pay.toString()
                    txtRecyclerPaymentMonth.text = itemEntity.dateForMonth
                }
            }
        }

    fun setData(payments: List<Payment>) {
        this.payments = payments
        notifyDataSetChanged()
    }
}