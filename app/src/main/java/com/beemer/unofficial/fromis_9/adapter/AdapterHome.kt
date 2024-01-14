package com.beemer.unofficial.fromis_9.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.data.DataHome
import com.beemer.unofficial.fromis_9.databinding.RowHomeBinding
import com.beemer.unofficial.fromis_9.diff.HomeDiffUtil
import com.bumptech.glide.Glide

class AdapterHome : RecyclerView.Adapter<AdapterHome.ViewHolder>() {
    private var itemList = mutableListOf<DataHome>()
    private var onItemClickListener: ((DataHome, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowHomeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: DataHome) {
            Glide.with(binding.root).load(item.image).placeholder(ColorDrawable(Color.TRANSPARENT)).into(binding.imgHome)
            binding.txtHome.text = item.title
        }
    }

    fun setOnItemClickListener(listener: (DataHome, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setHome(newHome: List<DataHome>) {
        val diffCallback = HomeDiffUtil(itemList, newHome)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newHome)
        diffResult.dispatchUpdatesTo(this)
    }
}