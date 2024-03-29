package com.beemer.unofficial.fromis_9.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.data.DataTrackList
import com.beemer.unofficial.fromis_9.databinding.RowAlbumTracklistBinding
import com.beemer.unofficial.fromis_9.diff.TrackListDiffUtil

class AdapterTrackList : RecyclerView.Adapter<AdapterTrackList.ViewHolder>() {
    private var itemList = mutableListOf<DataTrackList>()
    private var onItemClickListener: ((DataTrackList, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAlbumTracklistBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowAlbumTracklistBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: DataTrackList) {
            binding.txtTrackNumber.text = item.trackNumber.toString()
            binding.txtSongName.text = item.songName
            binding.txtSongLength.text = item.songLength
            binding.viewTitleTrack.apply {
                setBackgroundColor(Color.parseColor("#${item.colorMain}"))
                visibility = if (item.titleTrack) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    fun setOnItemClickListener(listener: (DataTrackList, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setTrackList(newTrackList: List<DataTrackList>) {
        val diffCallback = TrackListDiffUtil(itemList, newTrackList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newTrackList)
        diffResult.dispatchUpdatesTo(this)
    }
}