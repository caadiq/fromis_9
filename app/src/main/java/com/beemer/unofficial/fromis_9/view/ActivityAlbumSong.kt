package com.beemer.unofficial.fromis_9.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumSongBinding
import com.beemer.unofficial.fromis_9.repository.RepositoryAlbumList
import com.beemer.unofficial.fromis_9.service.RetrofitService
import com.beemer.unofficial.fromis_9.utils.OpenYouTube
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelFactory
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelSong

class ActivityAlbumSong : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumSongBinding.inflate(layoutInflater) }

    private val viewModel: ViewModelSong by lazy { ViewModelProvider(this, ViewModelFactory(RepositoryAlbumList(RetrofitService.apiAlbumList)))[ViewModelSong::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewModel()
        setupFAB()
    }

    private fun setupViewModel() {
        val songName = intent.getStringExtra("songName")
        val albumName = intent.getStringExtra("albumName")

        if (albumName != null && songName != null) {
            viewModel.getSong(albumName, songName)
            binding.txtSongName.text = songName

            viewModel.apply {
                lyricist.observe(this@ActivityAlbumSong) { binding.txtLyricist.text = addMiddleDot(it) }
                composer.observe(this@ActivityAlbumSong) { binding.txtComposer.text = addMiddleDot(it) }
                arranger.observe(this@ActivityAlbumSong) {
                    if (it != null) {
                        binding.txtArranger.text = addMiddleDot(it)
                    } else {
                        binding.txtTitleArranger.visibility = View.GONE
                        binding.cardArranger.visibility = View.GONE
                    }
                }
                lyrics.observe(this@ActivityAlbumSong) { binding.txtLyrics.text = it }
                errorMessage.observe(this@ActivityAlbumSong) { message ->
                    message.getContentIfNotHandled()?.let { Toast.makeText(this@ActivityAlbumSong, it, Toast.LENGTH_SHORT).show() }
                }
            }
        }
    }

    private fun setupFAB() {
        val colorPrimary = intent.getStringExtra("colorPrimary")
        val colorSecondary = intent.getStringExtra("colorSecondary")
        val videoId = intent.getStringExtra("videoId")

        binding.fabPlay.apply {
            supportBackgroundTintList = ColorStateList.valueOf(Color.parseColor("#$colorPrimary"))
            supportImageTintList = ColorStateList.valueOf(Color.parseColor("#$colorSecondary"))
            setOnClickListener { videoId?.let { OpenYouTube.openYoutube(this@ActivityAlbumSong, it) } }
        }

        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                binding.fabPlay.hide()
            } else {
                binding.fabPlay.show()
            }
        }
    }

    private fun addMiddleDot(originalText: String): String {
        return originalText.split("\n").mapIndexed { _, line -> "· $line" }.joinToString(separator = "\n")
    }
}