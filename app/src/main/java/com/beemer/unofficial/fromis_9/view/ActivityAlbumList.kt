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
import com.beemer.unofficial.fromis_9.viewmodel.ViewModelFactoryAlbumList

class ActivityAlbumList : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumListBinding.inflate(layoutInflater) }

    private val viewModel: ViewModelAlbumList by lazy {
        val apiAlbumList = RetrofitService.apiAlbumList
        val repository = RepositoryAlbumList(apiAlbumList, this)
        val factory = ViewModelFactoryAlbumList(repository)
        ViewModelProvider(this, factory)[ViewModelAlbumList::class.java]
    }

    private val btnToggleGroup by lazy { binding.btnToggleGroup }
    private val btnSort by lazy { binding.btnSort }
    private val recyclerView by lazy { binding.recyclerView }

    private val adapterAlbumList by lazy { AdapterAlbumList() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.apply {
            getAlbumList()

            sortBy.observe(this@ActivityAlbumList) {
                btnToggleGroup.check(
                    when (it) {
                        "release" -> R.id.btnRelease
                        "title" -> R.id.btnTitle
                        "type" -> R.id.btnType
                        else -> R.id.btnRelease
                    }
                )
            }
            isAscending.observe(this@ActivityAlbumList) { setOrderButtonImage(it) }
            albumList.observe(this@ActivityAlbumList) { adapterAlbumList.setAlbumList(it ?: emptyList()) }
            errorMessage.observe(this@ActivityAlbumList) {
                it.getContentIfNotHandled()?.let { message ->
                    Toast.makeText(this@ActivityAlbumList, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                viewModel.updateSortBy(
                    when (checkedId) {
                        R.id.btnRelease -> "release"
                        R.id.btnTitle -> "title"
                        R.id.btnType -> "type"
                        else -> "release"
                    }
                )
            }
        }

        btnSort.setOnClickListener {
            viewModel.updateIsAscending(!(viewModel.isAscending.value ?: true))
        }

        recyclerView.apply {
            adapter = adapterAlbumList
            setHasFixedSize(true)
        }

        adapterAlbumList.setOnItemClickListener { item, _ ->
            startActivity(Intent(this, ActivityAlbum::class.java).apply {
                putExtra("albumName", item.albumName)
                putExtra("albumArt", item.albumArt)
                putExtra("colorMain", item.colorMain)
            })
        }
    }

    // 오름차순-내림차순 버튼 이미지 변경
    private fun setOrderButtonImage(isAscending: Boolean) {
        btnSort.setImageResource(if (isAscending) R.drawable.icon_ascending else R.drawable.icon_descending)
    }
}