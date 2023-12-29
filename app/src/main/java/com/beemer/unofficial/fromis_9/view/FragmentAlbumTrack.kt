package com.beemer.unofficial.fromis_9.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.beemer.unofficial.fromis_9.adapter.AdapterTrackList
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumTrackBinding

class FragmentAlbumTrack : Fragment() {
    private val binding by lazy { FragmentAlbumTrackBinding.inflate(layoutInflater) }
    private lateinit var activityAlbum: ActivityAlbum

    private val recyclerView by lazy { binding.recyclerView }

    private val adapterTrackList by lazy { AdapterTrackList() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityAlbum = context as ActivityAlbum
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        activityAlbum.viewModel.trackList.observe(activityAlbum) {
            adapterTrackList.setTrackList(it ?: emptyList())
            Log.d("테스트", "FragmentAlbumTrack -trackList: ${it ?: emptyList()}")
        }

        recyclerView.apply {
            adapter = adapterTrackList
            setHasFixedSize(true)
        }

        adapterTrackList.setOnItemClickListener { item, _ ->
            startActivity(Intent(activityAlbum, ActivityAlbumSong::class.java).apply {
                putExtra("songname", item.songName)
            })
        }
        return binding.root
    }
}