package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumSongBinding

class ActivityAlbumSong : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumSongBinding.inflate(layoutInflater) }

    private val songName by lazy { intent.getStringExtra("songname") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.txtSongName.text = songName
    }
}