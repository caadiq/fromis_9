package com.beemer.unofficial.fromis_9.view.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.beemer.unofficial.fromis_9.databinding.FragmentAlbumDetailsTrackBinding
import com.beemer.unofficial.fromis_9.view.adapter.AlbumTrackListAdapter
import com.beemer.unofficial.fromis_9.viewmodel.AlbumViewModel

class AlbumDetailsTrackFragment : Fragment() {
    private var _binding: FragmentAlbumDetailsTrackBinding? = null
    private val binding get() = _binding!!

    private val albumViewModel: AlbumViewModel by activityViewModels()

    private lateinit var albumTrackListAdapter: AlbumTrackListAdapter

    companion object {
        fun newInstance(colorMain: String?): AlbumDetailsTrackFragment {
            val fragment = AlbumDetailsTrackFragment()
            val args = Bundle()
            args.putString("colorMain", colorMain)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAlbumDetailsTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        albumTrackListAdapter = AlbumTrackListAdapter(arguments?.getString("colorMain"))

        binding.recyclerView.apply {
            adapter = albumTrackListAdapter
            setHasFixedSize(true)
        }

        albumTrackListAdapter.setOnItemClickListener { item, _ ->
            val intent = Intent(requireContext(), AlbumSongActivity::class.java)
            intent.putExtra("songName", item.songName)
            intent.putExtra("colorMain", arguments?.getString("colorMain"))
            intent.putExtra("titleTrack", item.titleTrack)
            startActivity(intent)
        }
    }

    private fun setupViewModel() {
        albumViewModel.albumDetails.observe(viewLifecycleOwner) {
            albumTrackListAdapter.setItemList(it.trackList)
        }
    }
}