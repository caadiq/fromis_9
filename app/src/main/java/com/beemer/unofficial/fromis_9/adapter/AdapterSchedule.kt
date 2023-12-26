package com.beemer.unofficial.fromis_9.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.data.DataSchedule
import com.beemer.unofficial.fromis_9.databinding.RowScheduleBinding
import com.beemer.unofficial.fromis_9.diff.ScheduleDiffUtil
import com.bumptech.glide.Glide

class AdapterSchedule : RecyclerView.Adapter<AdapterSchedule.ViewHolder>() {
    private var itemList = mutableListOf<DataSchedule>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowScheduleBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataSchedule) {
            Glide.with(binding.root).load(item.image).placeholder(ColorDrawable(Color.TRANSPARENT)).into(binding.icon)
            binding.schedule.text = item.schedule
            binding.time.text = item.time
            if (item.description.isNullOrEmpty()) {
                binding.description.visibility = View.GONE
            } else {
                binding.description.text = item.description
                binding.description.visibility = View.VISIBLE
            }
        }
    }

    fun setSchedule(newSchedule: List<DataSchedule>) {
        val diffCallback = ScheduleDiffUtil(itemList, newSchedule)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newSchedule)
        diffResult.dispatchUpdatesTo(this)
    }
}