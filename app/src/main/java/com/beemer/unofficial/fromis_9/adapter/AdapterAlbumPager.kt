package com.beemer.unofficial.fromis_9.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.beemer.unofficial.fromis_9.view.FragmentAlbumIntroduction
import com.beemer.unofficial.fromis_9.view.FragmentAlbumPhoto
import com.beemer.unofficial.fromis_9.view.FragmentAlbumTrack

class AdapterAlbumPager(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentAlbumIntroduction()
            1 -> FragmentAlbumPhoto()
            2 -> FragmentAlbumTrack()
            else -> throw IllegalStateException("잘못된 position")
        }
    }
}