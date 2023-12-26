package com.beemer.unofficial.fromis_9

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.beemer.unofficial.fromis_9.adapter.AdapterAlbumList
import com.beemer.unofficial.fromis_9.api.ApiAlbumList
import com.beemer.unofficial.fromis_9.data.DataAlbumList
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumListBinding
import com.beemer.unofficial.fromis_9.datastore.Preferences
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActivityAlbumList : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumListBinding.inflate(layoutInflater) }

    private val btnToggleGroup by lazy { binding.btnToggleGroup }
    private val btnSort by lazy { binding.btnSort }
    private val recyclerView by lazy { binding.recyclerView }

    private val adapterAlbumList by lazy { AdapterAlbumList() }
    private var albumList = listOf<DataAlbumList>()

    private val preference by lazy { Preferences }

    private val albumListApi: ApiAlbumList by lazy {
        val apiUrl = BuildConfig.API_URL
        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiAlbumList::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // preference값 가져와서 버튼 상태 설정
        lifecycleScope.launch {
            val currentSortBy = preference.getSortBy(this@ActivityAlbumList)
            val currentIsAscending = preference.getIsAscending(this@ActivityAlbumList)
            btnToggleGroup.check(if (currentSortBy == 0) btnToggleGroup.getChildAt(0).id else currentSortBy)
            setOrderButtonImage(currentIsAscending)
        }

        // 토글 버튼 체크 상태 변화 이벤트
        btnToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                lifecycleScope.launch {
                    preference.setSortBy(this@ActivityAlbumList, checkedId)
                    sortAlbumList()
                }
            }
        }

        btnSort.setOnClickListener {
            lifecycleScope.launch {
                val isAscending = preference.getIsAscending(this@ActivityAlbumList)
                preference.setIsAscending(this@ActivityAlbumList, !isAscending)
                setOrderButtonImage(!isAscending)
                sortAlbumList()
            }
        }

        recyclerView.apply {
            adapter = adapterAlbumList
            setHasFixedSize(true)
            //itemAnimator = null
        }

        getAlbumListFromApi()
    }

    private fun getAlbumListFromApi() {
        lifecycleScope.launch {
            try {
                val albumListResponse = albumListApi.getAlbumList("album,albumArt")
                albumList = albumListResponse.map {
                    DataAlbumList(
                        albumName = it.albumName ?: "",
                        albumType = it.albumType ?: "",
                        releaseDate = it.releaseDate ?: "",
                        colorPrimary = it.colorPrimary ?: "",
                        colorSecondary = it.colorSecondary ?: "",
                        albumArt = it.albumArt?.imageUrl ?: ""
                    )
                }
                sortAlbumList()
            } catch (e: Exception) {
                Toast.makeText(this@ActivityAlbumList, "앨범 목록을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sortAlbumList() {
        lifecycleScope.launch {
            val sortedList = when (preference.getSortBy(this@ActivityAlbumList)) {
                btnToggleGroup.getChildAt(0).id -> albumList.sortedBy { it.releaseDate }
                btnToggleGroup.getChildAt(1).id -> albumList.sortedBy { it.albumName.lowercase() }
                btnToggleGroup.getChildAt(2).id -> albumList.sortedWith(compareBy<DataAlbumList> { it.albumType }.thenBy { it.albumName.lowercase() })
                else -> albumList
            }

            val isAscending = preference.getIsAscending(this@ActivityAlbumList)
            val finalList = if (isAscending) sortedList else sortedList.reversed()
            adapterAlbumList.setAlbumList(finalList)
        }
    }

    // 오름차순-내림차순 버튼 이미지 변경
    private fun setOrderButtonImage(isAscending: Boolean) {
        btnSort.setImageResource(if (isAscending) R.drawable.icon_ascending else R.drawable.icon_descending)
    }
}