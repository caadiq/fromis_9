package com.beemer.unofficial.fromis_9.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.R
import com.beemer.unofficial.fromis_9.adapter.AdapterAlbumList
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumListBinding
import com.beemer.unofficial.fromis_9.repository.RepositoryAlbumList
import com.beemer.unofficial.fromis_9.service.RetrofitService
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelAlbumList
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelFactory

class ActivityAlbumList : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumListBinding.inflate(layoutInflater) }

    private val viewModel: ViewModelAlbumList by lazy { ViewModelProvider(this, ViewModelFactory(RepositoryAlbumList(RetrofitService.apiAlbumList)))[ViewModelAlbumList::class.java] }

    private val adapterAlbumList = AdapterAlbumList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewModel()
        setupToggleGroupListener()
        setupOrderButtonListener()
        setupRecyclerView()
    }

    private fun setupViewModel() {
        viewModel.apply {
            getAlbumList()

            sortBy.observe(this@ActivityAlbumList) { sortBy.observe(this@ActivityAlbumList) { setSortByList(it) } }
            isAscending.observe(this@ActivityAlbumList) { setOrderByButtonImage(it) }
            albumList.observe(this@ActivityAlbumList) { adapterAlbumList.setAlbumList(it ?: emptyList()) }
            errorMessage.observe(this@ActivityAlbumList) { message ->
                message.getContentIfNotHandled()?.let { Toast.makeText(this@ActivityAlbumList, it, Toast.LENGTH_SHORT).show() }
            }
        }
    }

    private fun setupToggleGroupListener() {
        binding.btnToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                val sortBy = when (checkedId) {
                    R.id.btnRelease -> "release"
                    R.id.btnTitle -> "title"
                    R.id.btnType -> "type"
                    else -> "release"
                }
                viewModel.updateSortBy(sortBy)
            }
        }
    }

    private fun setupOrderButtonListener() {
        binding.btnOrderBy.setOnClickListener {
            viewModel.updateIsAscending(!(viewModel.isAscending.value ?: true))
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = adapterAlbumList
            setHasFixedSize(true)
        }

        adapterAlbumList.setOnItemClickListener { item, _ ->
            val intent = Intent(this@ActivityAlbumList, ActivityAlbum::class.java).apply {
                putExtra("albumName", item.albumName)
                putExtra("albumArt", item.albumArt)
                putExtra("colorMain", item.colorMain)
            }
            startActivity(intent)
        }
    }

    private fun setSortByList(sortBy: String) {
        val buttonId = when (sortBy) {
            "release" -> R.id.btnRelease
            "title" -> R.id.btnTitle
            "type" -> R.id.btnType
            else -> R.id.btnRelease
        }
        binding.btnToggleGroup.check(buttonId)
    }

    // 오름차순-내림차순 버튼 이미지 변경
    private fun setOrderByButtonImage(isAscending: Boolean) {
        val icon = if (isAscending) R.drawable.icon_ascending else R.drawable.icon_descending
        binding.btnOrderBy.setImageResource(icon)
    }
}