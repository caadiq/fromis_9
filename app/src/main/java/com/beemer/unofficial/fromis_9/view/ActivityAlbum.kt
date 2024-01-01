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
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelFactory
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

class ActivityAlbum : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumBinding.inflate(layoutInflater) }

    val viewModel: ViewModelAlbum by lazy { ViewModelProvider(this, ViewModelFactory(RepositoryAlbumList(RetrofitService.apiAlbumList, this)))[ViewModelAlbum::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val albumName by lazy { intent.getStringExtra("albumName") }
        val albumArt by lazy { intent.getStringExtra("albumArt") }
        val colorMain by lazy { intent.getStringExtra("colorMain") }

        Glide.with(this).load(albumArt).placeholder(ColorDrawable(Color.TRANSPARENT)).into(binding.imgAlbumArt)

        viewModel.apply {
            albumName?.let { getAlbum(it) }
            errorMessage.observe(this@ActivityAlbum) { message ->
                message.getContentIfNotHandled()?.let { Toast.makeText(this@ActivityAlbum, it, Toast.LENGTH_SHORT).show() }
            }
        }

        colorMain?.let { setupTabLayout(it) }
    }

    private fun setupTabLayout(colorMain: String) {
        val colorMainParsed = Color.parseColor("#${colorMain}")

        val tabTitles = listOf("소개", "사진", "트랙")
        val tabIcons = listOf(R.drawable.icon_introduction, R.drawable.icon_photo, R.drawable.icon_track)

        binding.viewPager.adapter = AdapterAlbumPager(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
            tab.icon = AppCompatResources.getDrawable(this, tabIcons[position])
        }.attach()

        val states = arrayOf(intArrayOf(android.R.attr.state_selected), intArrayOf(-android.R.attr.state_selected))
        val colors = intArrayOf(colorMainParsed, getColor(R.color.gray))
        val colorStateList = ColorStateList(states, colors)

        binding.tabLayout.apply {
            setSelectedTabIndicatorColor(colorMainParsed)
            setTabTextColors(getColor(R.color.gray), colorMainParsed)
            tabIconTint = colorStateList
        }
    }
}