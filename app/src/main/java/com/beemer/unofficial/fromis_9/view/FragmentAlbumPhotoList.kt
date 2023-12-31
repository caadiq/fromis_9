package com.beemer.unofficial.fromis_9.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.beemer.unofficial.fromis_9.adapter.AdapterPhotoList
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumPhotolistBinding

class FragmentAlbumPhotoList : Fragment() {
    private val binding by lazy { FragmentAlbumPhotolistBinding.inflate(layoutInflater) }
    private lateinit var activityAlbum: ActivityAlbum

    private val recyclerView by lazy { binding.recyclerView }

    private val adapterPhotoList by lazy { AdapterPhotoList() }
    private val gridLayoutmanager by lazy { GridLayoutManager(activityAlbum, 2) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityAlbum = context as ActivityAlbum
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityAlbum.viewModel.photoList.observe(activityAlbum) {
            adapterPhotoList.setPhotoList(it ?: emptyList())
        }

        recyclerView.apply {
            layoutManager = gridLayoutmanager
            adapter = adapterPhotoList
            setHasFixedSize(true)
        }

        adapterPhotoList.setOnItemClickListener { item, _ ->
            startActivity(Intent(activityAlbum, ActivityAlbumSong::class.java).apply {
                putExtra("concept", item.concept)
                putExtra("imageUrl", item.imageUrl)
            })
        }

        return binding.root
    }
}