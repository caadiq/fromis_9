package com.beemer.unofficial.fromis_9.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.adapter.AdapterVideoList
import com.beemer.unofficial.fromis_9.databinding.FragmentMainVideoBinding
import com.beemer.unofficial.fromis_9.repository.RepositoryVideoList
import com.beemer.unofficial.fromis_9.service.RetrofitService
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelFactory
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelVideoList

class FragmentMainVideo : Fragment() {
    private var _binding: FragmentMainVideoBinding? = null
    private val binding get() = _binding!!

    private lateinit var activityMain: ActivityMain
    private lateinit var adapterVideoList: AdapterVideoList
    private val viewModel: ViewModelVideoList by lazy { ViewModelProvider(this, ViewModelFactory(RepositoryVideoList(RetrofitService.apiVideoList)))[ViewModelVideoList::class.java] }

    private lateinit var searchMenuItem: MenuItem
    private lateinit var searchView: SearchView

    private var toolbarTitle: String? = null
    private var loadingState = false
    private var currentType = "all"

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityMain = context as ActivityMain
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainVideoBinding.inflate(inflater, container, false)

        initToolbar()
        initMenu()
        initToggleButton()
        initRecyclerView()
        initViewModel()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initToolbar() {
        toolbarTitle = getString(R.string.str_fragment_main_video_title)
        binding.toolbar.title = toolbarTitle
        activityMain.setSupportActionBar(binding.toolbar)
    }

    private fun initMenu() {
        activityMain.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main_video_search, menu)
                searchMenuItem = menu.findItem(R.id.search)
                searchView = searchMenuItem.actionView as SearchView

                searchView.queryHint = getString(R.string.str_fragment_main_video_search)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        binding.swipeRefreshLayout.isRefreshing = true
                        viewModel.getVideoList(binding.recyclerView, currentType, query)
                        searchView.clearFocus()
                        toolbarTitle = query
                        binding.toolbar.title = toolbarTitle
                        binding.toolbar.collapseActionView()
                        return false
                    }

                    override fun onQueryTextChange(newText: String): Boolean {
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initToggleButton() {
        binding.btnToggleGroup.apply {
            selectButton(binding.btnAll)
            viewModel.updateVideoType(currentType)

            binding.btnToggleGroup.setOnSelectListener {
                viewModel.updateVideoType(
                    when (it) {
                        binding.btnAll -> "all"
                        binding.btnMV -> "mv"
                        binding.btnChannel9 -> "channel9"
                        binding.btnFM124 -> "fm124"
                        binding.btn9log -> "9log"
                        binding.btnFromisoda -> "fromisoda"
                        else -> "all"
                    }
                )
            }
        }
    }

    private fun initRecyclerView() {
        adapterVideoList = AdapterVideoList(binding.recyclerView)
        binding.recyclerView.adapter = adapterVideoList
        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)?.findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (!recyclerView.canScrollVertically(1) && lastVisibleItemPosition == itemTotalCount && !loadingState) {
                    viewModel.getNextVideoList()
                }
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.resetSearchQuery()
            viewModel.getVideoList(binding.recyclerView, currentType, null)
            binding.toolbar.title = getString(R.string.str_fragment_main_video_title)
        }
    }

    private fun initViewModel() {
        viewModel.apply {
            videoType.observe(viewLifecycleOwner) {
                currentType = it
                updateToolbarTitle()
                viewModel.getVideoList(binding.recyclerView, it, null)
                binding.swipeRefreshLayout.isRefreshing = true
            }
            videoList.observe(viewLifecycleOwner) {
                adapterVideoList.setVideoList(it ?: emptyList())
                binding.swipeRefreshLayout.isRefreshing = false
            }
            searchQuery.observe(viewLifecycleOwner) {
                it?.let { query -> toolbarTitle = query }
            }
            isLoading.observe(viewLifecycleOwner) {
                adapterVideoList.setLoading(it)
                loadingState = it
            }
            errorMessage.observe(viewLifecycleOwner) { message ->
                message.getContentIfNotHandled()?.let { Toast.makeText(activityMain, it, Toast.LENGTH_SHORT).show() }
            }
        }
    }

    private fun updateToolbarTitle() {
        val buttonMap = mapOf(
            "all" to R.string.str_fragment_main_video_title,
            "mv" to R.string.str_fragment_main_video_mv,
            "channel9" to R.string.str_fragment_main_video_channel9,
            "fm124" to R.string.str_fragment_main_video_fm124,
            "9log" to R.string.str_fragment_main_video_9log,
            "fromisoda" to R.string.str_fragment_main_video_fromisoda
        )

        if (toolbarTitle.isNullOrEmpty()) {
            val titleRes = buttonMap[currentType] ?: R.string.str_fragment_main_video_title
            binding.toolbar.title = getString(titleRes)
        } else {
            binding.toolbar.title = toolbarTitle
        }
    }
}