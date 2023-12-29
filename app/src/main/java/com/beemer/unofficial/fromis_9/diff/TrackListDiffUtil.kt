package com.beemer.unofficial.fromis_9.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.unofficial.fromis_9.data.DataTrackList

class TrackListDiffUtil(private val oldList: List<DataTrackList>, private val newList: List<DataTrackList>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].songName == newList[newItemPosition].songName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}