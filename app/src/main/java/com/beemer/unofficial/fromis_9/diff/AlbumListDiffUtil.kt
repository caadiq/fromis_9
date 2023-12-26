package com.beemer.unofficial.fromis_9.diff

import androidx.recyclerview.widget.DiffUtil
import com.beemer.unofficial.fromis_9.data.DataAlbumList

class AlbumListDiffUtil(private val oldList: List<DataAlbumList>, private val newList: List<DataAlbumList>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].albumName == newList[newItemPosition].albumName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}