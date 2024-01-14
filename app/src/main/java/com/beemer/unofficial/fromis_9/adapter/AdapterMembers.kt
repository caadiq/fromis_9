package com.beemer.unofficial.fromis_9.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.data.DataMembers
import com.beemer.unofficial.fromis_9.databinding.RowMembersBinding
import com.beemer.unofficial.fromis_9.diff.MembersDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AdapterMembers : RecyclerView.Adapter<AdapterMembers.ViewHolder>() {
    private var itemList = mutableListOf<DataMembers>()
    private var onItemClickListener: ((DataMembers, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowMembersBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowMembersBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: DataMembers) {
            Glide.with(binding.root).load(item.imageUrl).placeholder(ColorDrawable(Color.TRANSPARENT)).transition(DrawableTransitionOptions.withCrossFade()).into(binding.imgProfile)
            binding.txtName.text = item.name
        }
    }

    fun setOnItemClickListener(listener: (DataMembers, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setMembers(newMember: List<DataMembers>) {
        val diffCallback = MembersDiffUtil(itemList, newMember)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newMember)
        diffResult.dispatchUpdatesTo(this)
    }
}