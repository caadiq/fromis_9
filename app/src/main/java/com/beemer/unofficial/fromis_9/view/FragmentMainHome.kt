package com.beemer.unofficial.fromis_9.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.adapter.AdapterHome
import com.beemer.unofficial.fromis_9.data.DataHome
import com.beemer.unofficial.fromis_9.databinding.FragmentMainHomeBinding

class FragmentMainHome : Fragment() {
    private var _binding: FragmentMainHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityMain: ActivityMain
    private lateinit var gridLayoutManager: GridLayoutManager
    private val adapterHome = AdapterHome()
    private val homeItemList = mutableListOf<DataHome>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityMain = context as ActivityMain
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainHomeBinding.inflate(inflater, container, false)

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(activityMain, ActivitySettings::class.java))
        }

        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) 2 else 1
            }
        }

        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = adapterHome
            itemAnimator = null
        }

        homeItemList.apply {
            add(DataHome(image = R.drawable.image_fromis9, title = "프로미스나인"))
            add(DataHome(image = R.drawable.image_album, title = "앨범"))
            add(DataHome(image = R.drawable.image_flover, title = "응원법"))
        }

        adapterHome.apply {
            setHome(homeItemList)
            setOnItemClickListener { _, position ->
                when (position) {
                    0 -> startActivity(Intent(activityMain, ActivityIntroduction::class.java))
                    1 -> startActivity(Intent(activityMain, ActivityAlbumList::class.java))
                    2 -> Toast.makeText(context, "응원법", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}