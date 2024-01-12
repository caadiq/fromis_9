package com.beemer.unofficial.fromis_9.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.unofficial.fromis_9.data.DataMembers

class MembersDiffUtil(private val oldList: List<DataMembers>, private val newList: List<DataMembers>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}