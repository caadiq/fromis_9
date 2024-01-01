package com.beemer.unofficial.fromis_9.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumSongBinding
import com.beemer.unofficial.fromis_9.repository.RepositoryAlbumList
import com.beemer.unofficial.fromis_9.service.RetrofitService
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelFactory
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelSong

class ActivityAlbumSong : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumSongBinding.inflate(layoutInflater) }

    private val viewModel: ViewModelSong by lazy {
        val apiAlbumList = RetrofitService.apiAlbumList
        val repository = RepositoryAlbumList(apiAlbumList, this)
        val factory = ViewModelFactory(repository)
        ViewModelProvider(this, factory)[ViewModelSong::class.java]
    }

    private val txtSongName by lazy { binding.txtSongName }
    private val txtLyricist by lazy { binding.txtLyricist }
    private val txtComposer by lazy { binding.txtComposer }
    private val txtArranger by lazy { binding.txtArranger }
    private val txtLyrics by lazy { binding.txtLyrics }
    private val txtTitleArranger by lazy { binding.txtTitleArranger }
    private val cardArranger by lazy { binding.cardArranger }

    private val songName by lazy { intent.getStringExtra("songName") }
    private val albumName by lazy { intent.getStringExtra("albumName") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.apply {
            if (albumName != null && songName != null)
                getSong(albumName!!, songName!!)

            lyricist.observe(this@ActivityAlbumSong) { txtLyricist.text = addMiddleDot(it) }
            composer.observe(this@ActivityAlbumSong) { txtComposer.text = addMiddleDot(it) }
            arranger.observe(this@ActivityAlbumSong) {
                if (it != null) {
                    txtArranger.text = addMiddleDot(it)
                } else {
                    txtTitleArranger.visibility = View.GONE
                    cardArranger.visibility = View.GONE
                }
            }
            lyrics.observe(this@ActivityAlbumSong) { txtLyrics.text = it }
            errorMessage.observe(this@ActivityAlbumSong) {
                it.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(this@ActivityAlbumSong, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        txtSongName.text = songName
    }

    private fun addMiddleDot(originalText: String): String {
        return originalText.split("\n").mapIndexed { _, line -> "· $line" }.joinToString(separator = "\n")
    }
}