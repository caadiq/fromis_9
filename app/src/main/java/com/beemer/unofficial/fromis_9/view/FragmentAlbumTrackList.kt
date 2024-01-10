package com.beemer.unofficial.fromis_9.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.beemer.unofficial.fromis_9.adapter.AdapterTrackList
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumTracklistBinding

class FragmentAlbumTrackList : Fragment() {
    private var _binding: FragmentAlbumTracklistBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityAlbum: ActivityAlbum
    private val adapterTrackList = AdapterTrackList()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityAlbum = context as ActivityAlbum
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumTracklistBinding.inflate(inflater, container, false)

        activityAlbum.viewModel.trackList.observe(viewLifecycleOwner) {
            adapterTrackList.setTrackList(it ?: emptyList())
        }

        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = adapterTrackList
            setHasFixedSize(true)
        }

        adapterTrackList.setOnItemClickListener { item, _ ->
            val intent = Intent(activityAlbum, ActivityAlbumSong::class.java).apply {
                putExtra("albumName", item.albumName)
                putExtra("songName", item.songName)
                putExtra("colorPrimary", item.colorPrimary)
                putExtra("colorSecondary", item.colorSecondary)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}