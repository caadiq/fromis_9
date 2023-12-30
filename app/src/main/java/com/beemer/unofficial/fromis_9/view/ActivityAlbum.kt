package com.beemer.unofficial.fromis_9.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.adapter.AdapterAlbumPager
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumBinding
import com.beemer.unofficial.fromis_9.repository.RepositoryAlbumList
import com.beemer.unofficial.fromis_9.service.RetrofitService
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelAlbum
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelFactoryAlbum
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class ActivityAlbum : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumBinding.inflate(layoutInflater) }

    val viewModel: ViewModelAlbum by lazy {
        val apiAlbumList = RetrofitService.apiAlbumList
        val repository = RepositoryAlbumList(apiAlbumList, this)
        val factory = ViewModelFactoryAlbum(repository)
        ViewModelProvider(this, factory)[ViewModelAlbum::class.java]
    }

    private val imgAlbumArt by lazy { binding.imgAlbumArt }
    private val tabLayout by lazy { binding.tabLayout }
    private val viewPager by lazy { binding.viewPager }

    private val albumName by lazy { intent.getStringExtra("albumName") }
    private val albumArt by lazy { intent.getStringExtra("albumArt") }
    private val colorMain by lazy { intent.getStringExtra("colorMain") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Glide.with(this).load(albumArt).placeholder(ColorDrawable(Color.TRANSPARENT)).into(imgAlbumArt)

        viewModel.apply {
            albumName?.let { getAlbum(it) }
            errorMessage.observe(this@ActivityAlbum) {
                it.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(this@ActivityAlbum, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewPager.adapter = AdapterAlbumPager(this)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "소개"
                    tab.icon = AppCompatResources.getDrawable(this, R.drawable.icon_introduction)
                }
                1 -> {
                    tab.text = "사진"
                    tab.icon = AppCompatResources.getDrawable(this, R.drawable.icon_photo)
                }
                2 -> {
                    tab.text = "트랙"
                    tab.icon = AppCompatResources.getDrawable(this, R.drawable.icon_track)
                }
            }
        }.attach()

        tabLayout.apply {
            val colorMainParsed = Color.parseColor("#${colorMain}")
            val states = arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected))
            val colors = intArrayOf(colorMainParsed, getColor(R.color.gray))
            val colorStateList = ColorStateList(states, colors)

            setSelectedTabIndicatorColor(colorMainParsed)
            setTabTextColors(getColor(R.color.gray), colorMainParsed)
            tabIconTint = colorStateList
        }
    }
}