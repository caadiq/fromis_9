package com.beemer.unofficial.fromis_9

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumBinding
import com.beemer.unofficial.fromis_9.datastore.Preferences
import kotlinx.coroutines.launch


class ActivityAlbum : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumBinding.inflate(layoutInflater) }

    private val btnToggleGroup by lazy { binding.btnToggleGroup }
    private val btnSort by lazy { binding.btnSort }

    private val preference by lazy { Preferences }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // preference값 가져와서 버튼 상태 설정
        lifecycleScope.launch {
            val currentSortBy = preference.getSortBy(this@ActivityAlbum)
            val currentIsAscending = preference.getIsAscending(this@ActivityAlbum)
            btnToggleGroup.check(if (currentSortBy == 0) btnToggleGroup.getChildAt(0).id else currentSortBy)
            setOrderButtonImage(currentIsAscending)
        }

        btnToggleGroup.addOnButtonCheckedListener { _, checkedId, _ ->
            lifecycleScope.launch {
                preference.setSortBy(this@ActivityAlbum, checkedId)
            }
        }

        btnSort.setOnClickListener {
            lifecycleScope.launch {
                val isAscending = preference.getIsAscending(this@ActivityAlbum)
                preference.setIsAscending(this@ActivityAlbum, !isAscending)
                setOrderButtonImage(!isAscending)
            }
        }
    }

    // 오름차순-내림차순 버튼 이미지 변경
    private fun setOrderButtonImage(isAscending: Boolean) {
        btnSort.setImageResource(if (isAscending) R.drawable.icon_ascending else R.drawable.icon_descending)
    }
}