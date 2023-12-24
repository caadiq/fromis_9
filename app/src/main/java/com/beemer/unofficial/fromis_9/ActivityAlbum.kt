package com.beemer.unofficial.fromis_9

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beemer.unofficial.fromis_9.databinding.ActivityAlbumBinding
import com.beemer.unofficial.fromis_9.preference.Preference


class ActivityAlbum : AppCompatActivity() {
    private val binding by lazy { ActivityAlbumBinding.inflate(layoutInflater) }

    private val btnToggleGroup by lazy { binding.btnToggleGroup }
    private val btnRelease by lazy { binding.btnRelease }
    private val btnTitle by lazy { binding.btnTitle }
    private val btnClassify by lazy { binding.btnClassify }
    private val btnSort by lazy { binding.btnSort }

    private val preference by lazy { Preference }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        btnToggleGroup.apply {
            check(
                when (preference.sortBy) {
                    "release" -> btnRelease.id
                    "title" -> btnTitle.id
                    "classify" -> btnClassify.id
                    else -> btnRelease.id
                }
            )

            addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    val selected = when (checkedId) {
                        btnRelease.id -> "release"
                        btnTitle.id -> "title"
                        btnClassify.id -> "classify"
                        else -> preference.sortBy
                    }
                    preference.sortBy = selected
                }
            }
        }

        btnSort.apply {
            setSortButtonImage()
            setOnClickListener {
                preference.isAscending = !preference.isAscending
                setSortButtonImage()
            }
        }
    }

    private fun setSortButtonImage() {
        btnSort.setImageResource(if (preference.isAscending) R.drawable.icon_ascending else R.drawable.icon_descending)
    }
}