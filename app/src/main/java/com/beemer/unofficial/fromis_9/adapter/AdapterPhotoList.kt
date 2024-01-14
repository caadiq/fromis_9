package com.beemer.unofficial.fromis_9.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.data.DataPhotoList
import com.beemer.unofficial.fromis_9.databinding.RowAlbumPhotolistBinding
import com.beemer.unofficial.fromis_9.diff.PhotoListDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class AdapterPhotoList : RecyclerView.Adapter<AdapterPhotoList.ViewHolder>() {
    private var itemList = mutableListOf<DataPhotoList>()
    private var onItemClickListener: ((DataPhotoList, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAlbumPhotolistBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowAlbumPhotolistBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: DataPhotoList) {
            binding.progressIndicator.show()

            Glide.with(binding.root).load(item.imageUrl)
                .placeholder(ColorDrawable(Color.TRANSPARENT))
                .transition(DrawableTransitionOptions.withCrossFade())
                .sizeMultiplier(0.5f)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                        binding.progressIndicator.hide()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        binding.progressIndicator.hide()
                        return false
                    }
                })
                .into(binding.imgPhoto)
        }
    }

    fun setOnItemClickListener(listener: (DataPhotoList, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setPhotoList(newPhotoList: List<DataPhotoList>) {
        val diffCallback = PhotoListDiffUtil(itemList, newPhotoList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newPhotoList)
        diffResult.dispatchUpdatesTo(this)
    }
}