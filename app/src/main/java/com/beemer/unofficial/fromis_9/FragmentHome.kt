package com.beemer.unofficial.fromis_9

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
import com.beemer.unofficial.fromis_9.adapter.AdapterHome
import com.beemer.unofficial.fromis_9.data.DataHome
import com.beemer.unofficial.fromis_9.databinding.FragmentHomeBinding

class FragmentHome : Fragment() {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var activityMain: ActivityMain

    private val recyclerView by lazy { binding.recyclerView }

    private val adapterHome by lazy { AdapterHome() }
    private lateinit var gridLayoutmanager: GridLayoutManager
    private val homeItemList = mutableListOf<DataHome>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityMain = context as ActivityMain
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        gridLayoutmanager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = object :SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (position == 0)
                        return 2
                    return 1
                }
            }
        }

        recyclerView.apply {
            layoutManager = gridLayoutmanager
            adapter = adapterHome
        }

        homeItemList.apply {
            add(DataHome(image = R.drawable.image_fromis9, title = "프로미스나인"))
            add(DataHome(image = R.drawable.image_album_unlock_my_world, title = "앨범"))
        }

        adapterHome.apply {
            itemList = homeItemList
            notifyDataSetChanged()

            setOnItemClickListener { _, position ->
                when (position) {
                    0 -> Toast.makeText(context, "프로미스나인", Toast.LENGTH_SHORT).show()
                    1 -> startActivity(Intent(activityMain, ActivityAlbum::class.java))
                }
            }
        }

        return binding.root
    }
}