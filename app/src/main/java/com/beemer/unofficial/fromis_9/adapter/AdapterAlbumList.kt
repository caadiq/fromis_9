package com.beemer.unofficial.fromis_9.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.data.DataAlbumList
import com.beemer.unofficial.fromis_9.databinding.RowAlbumListBinding
import com.beemer.unofficial.fromis_9.diff.AlbumListDiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class AdapterAlbumList : RecyclerView.Adapter<AdapterAlbumList.ViewHolder>() {
    var itemList = mutableListOf<DataAlbumList>()
    private var onItemClickListener: ((DataAlbumList, Int) -> Unit)? = null

    override fun getItemCount(): Int = itemList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RowAlbumListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class ViewHolder(private val binding: RowAlbumListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.invoke(itemList[position], position)
                }
            }
        }

        fun bind(item: DataAlbumList) {
            Glide.with(binding.root).load(item.albumArt).placeholder(ColorDrawable(Color.TRANSPARENT)).transition(DrawableTransitionOptions.withCrossFade()).sizeMultiplier(0.8f).into(binding.imgAlbumArt)
            binding.txtAlbumName.text = item.albumName
            binding.txtAlbumType.apply {
                text = item.albumType

                val colorPrimary = Color.parseColor("#${item.colorPrimary}")
                val colorSecondary = Color.parseColor("#${item.colorSecondary}")

                val backgroundDrawable = (AppCompatResources.getDrawable(context, R.drawable.drawable_album_list_type_background) as? GradientDrawable)?.mutate() as? GradientDrawable
                backgroundDrawable?.setColor(colorPrimary)

                background = backgroundDrawable
                setTextColor(colorSecondary)
            }
            binding.txtReleaseDate.text = item.releaseDate.replace("-", ".")
        }
    }

    fun setOnItemClickListener(listener: (DataAlbumList, Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setAlbumList(newAlbumList: List<DataAlbumList>) {
        val diffCallback = AlbumListDiffUtil(itemList, newAlbumList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newAlbumList)
        diffResult.dispatchUpdatesTo(this)
    }
}