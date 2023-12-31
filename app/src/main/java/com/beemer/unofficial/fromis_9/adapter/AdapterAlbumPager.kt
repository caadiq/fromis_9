package com.beemer.unofficial.fromis_9.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.beemer.unofficial.fromis_9.view.FragmentAlbumIntroduction
import com.beemer.unofficial.fromis_9.view.FragmentAlbumPhotoList
import com.beemer.unofficial.fromis_9.view.FragmentAlbumTrackList

class AdapterAlbumPager(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FragmentAlbumIntroduction()
            1 -> FragmentAlbumPhotoList()
            2 -> FragmentAlbumTrackList()
            else -> throw IllegalStateException("잘못된 position")
        }
    }
}