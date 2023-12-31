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
    private var _binding: FragmentAlbumPhotolistBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityAlbum: ActivityAlbum
    private lateinit var gridLayoutManager: GridLayoutManager
    private val adapterPhotoList = AdapterPhotoList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityAlbum = context as ActivityAlbum
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumPhotolistBinding.inflate(inflater, container, false)

        activityAlbum.viewModel.photoList.observe(activityAlbum) {
            adapterPhotoList.setPhotoList(it ?: emptyList())
        }

        gridLayoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = adapterPhotoList
            setHasFixedSize(true)
        }

        adapterPhotoList.setOnItemClickListener { item, _ ->
            startActivity(Intent(activityAlbum, ActivityAlbumPhoto::class.java).apply {
                putExtra("concept", item.concept)
                putExtra("imageUrl", item.imageUrl)
            })
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}