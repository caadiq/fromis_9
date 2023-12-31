package com.beemer.unofficial.fromis_9.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumIntroductionBinding

class FragmentAlbumIntroduction : Fragment() {
    private var _binding: FragmentAlbumIntroductionBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityAlbum: ActivityAlbum

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityAlbum = context as ActivityAlbum
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumIntroductionBinding.inflate(inflater, container, false)

        activityAlbum.viewModel.description.observe(activityAlbum) { binding.txtDescription.text = it }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}