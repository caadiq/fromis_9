package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.repository.RepositoryAlbumList
import com.beemer.unofficial.fromis_9.repository.RepositoryAppInfo
import com.beemer.unofficial.fromis_9.repository.RepositoryIntroduction
import com.beemer.unofficial.fromis_9.repository.RepositoryScheduleList
import com.beemer.unofficial.fromis_9.repository.RepositoryVideoList
import java.io.File

class ViewModelFactory(private val repository: Any) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ViewModelAlbum::class.java -> ViewModelAlbum(repository as RepositoryAlbumList) as T
            ViewModelAlbumList::class.java -> ViewModelAlbumList(repository as RepositoryAlbumList) as T
            ViewModelSong::class.java -> ViewModelSong(repository as RepositoryAlbumList) as T
            ViewModelScheduleList::class.java -> ViewModelScheduleList(repository as RepositoryScheduleList) as T
            ViewModelVideoList::class.java -> ViewModelVideoList(repository as RepositoryVideoList) as T
            ViewModelIntroduction::class.java -> ViewModelIntroduction(repository as RepositoryIntroduction) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

class ViewModelSettingsFactory(private val repository: RepositoryAppInfo, private val cacheDir: File, private val externalCacheDir: File?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelSettings::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelSettings(repository, cacheDir, externalCacheDir) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}