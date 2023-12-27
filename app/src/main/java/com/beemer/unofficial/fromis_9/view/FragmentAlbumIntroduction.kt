package com.beemer.unofficial.fromis_9.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumIntroductionBinding

class FragmentAlbumIntroduction : Fragment() {
    private val binding by lazy { FragmentAlbumIntroductionBinding.inflate(layoutInflater) }
    private lateinit var activityAlbum: ActivityAlbum

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityAlbum = context as ActivityAlbum
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


        return binding.root
    }
}