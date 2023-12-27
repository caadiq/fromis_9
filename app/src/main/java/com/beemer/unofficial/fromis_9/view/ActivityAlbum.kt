package com.beemer.unofficial.fromis_9.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.beemer.unofficial.fromis_9.BuildConfig
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.adapter.AdapterAlbumPager
import com.beemer.unofficial.fromis_9.api.ApiAlbumList
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumBinding
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActivityAlbum : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumBinding.inflate(layoutInflater) }

    private val imgAlbumArt by lazy { binding.imgAlbumArt }
    private val tabLayout by lazy { binding.tabLayout }
    private val viewPager by lazy { binding.viewPager }

    private val albumName by lazy { intent.getStringExtra("albumName") }
    private val albumArt by lazy { intent.getStringExtra("albumArt") }
    private val colorMain by lazy { intent.getStringExtra("colorMain") }

    private val albumListApi: ApiAlbumList by lazy {
        val apiUrl = BuildConfig.API_URL
        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiAlbumList::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Glide.with(this).load(albumArt).placeholder(ColorDrawable(Color.TRANSPARENT)).into(imgAlbumArt)

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

        getAlbumFromApi()
    }

    private fun getAlbumFromApi() {
        lifecycleScope.launch {
            try {
                val albumListResponse = albumListApi.getAlbumList("albumDescription,songList", albumName)

            } catch (e: Exception) {
                Toast.makeText(this@ActivityAlbum, "앨범 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}