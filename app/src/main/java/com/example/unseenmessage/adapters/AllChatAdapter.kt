package com.example.unseenmessage.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unseenmessage.R
import com.example.unseenmessage.interfaces.CallBack
import com.example.unseenmessage.models.ParentTable
import kotlinx.android.synthetic.main.chat_item.view.*

class AllChatAdapter(var callBack: CallBack) :
    RecyclerView.Adapter<AllChatAdapter.ViewHolder>() {

    private var students = emptyList<ParentTable>()


    fun setValues(states: List<ParentTable>) {
        this.students = states
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : AllChatAdapter.ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AllChatAdapter.ViewHolder, position: Int) {
        holder.user_name.text = students[position].user_name
        holder.user_sms.text = students[position].user_sms
        holder.user_time.text = students[position].time

        holder.itemView.setOnClickListener {

            callBack.click(position)
            Log.d("PPPPPCCCCCCCCC", "onBindViewHolder: clickkk")
        }
    }

    override fun getItemCount(): Int {
        return students.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val user_name = itemView.user_name
        val user_sms = itemView.user_sms
        val user_time = itemView.user_time


    }
}