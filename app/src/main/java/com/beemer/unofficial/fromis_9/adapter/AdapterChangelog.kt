package com.beemer.unofficial.fromis_9.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.data.DataChangelog
import com.beemer.unofficial.fromis_9.databinding.RowChangelogBinding
import com.beemer.unofficial.fromis_9.diff.ChangelogDiffUtil

class AdapterChangelog : RecyclerView.Adapter<AdapterChangelog.ViewHolder>() {
    private var itemList = mutableListOf<DataChangelog>()

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowChangelogBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowChangelogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataChangelog) {
            binding.txtVersion.text = item.version
            binding.txtRelease.text = item.release.replace("-", ".")
            binding.txtChangelog.text = item.changelog
        }
    }

    fun setChangelog(newChangelog: List<DataChangelog>) {
        val diffCallback = ChangelogDiffUtil(itemList, newChangelog)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newChangelog)
        diffResult.dispatchUpdatesTo(this)
    }
}