package com.beemer.unofficial.fromis_9.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.unofficial.fromis_9.data.DataVideoList

class VideoListDiffUtil(private val oldList: List<DataVideoList>, private val newList: List<DataVideoList>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].videoId == newList[newItemPosition].videoId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}