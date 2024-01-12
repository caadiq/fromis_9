package com.beemer.unofficial.fromis_9.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.data.DataBannerImage
import com.beemer.unofficial.fromis_9.databinding.RowBannerBinding
import com.bumptech.glide.Glide

class AdapterBannerImage(private val images: List<DataBannerImage>) : RecyclerView.Adapter<AdapterBannerImage.ViewHolder>() {
    override fun getItemCount(): Int = images.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowBannerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    inner class ViewHolder(private val binding: RowBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataBannerImage) {
            Glide.with(binding.root)
                .load(item.imageUrl)
                .placeholder(ColorDrawable(Color.TRANSPARENT))
                .sizeMultiplier(0.8f)
                .into(binding.imageView)
        }
    }
}