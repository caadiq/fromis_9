package com.beemer.unofficial.fromis_9.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.unofficial.fromis_9.data.DataChangelog

class ChangelogDiffUtil(private val oldList: List<DataChangelog>, private val newList: List<DataChangelog>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].version == newList[newItemPosition].version
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}