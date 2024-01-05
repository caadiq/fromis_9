package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.beemer.unofficial.fromis_9.repository.RepositoryAlbumList
import com.beemer.unofficial.fromis_9.repository.RepositoryScheduleList
import com.beemer.unofficial.fromis_9.repository.RepositoryVideoList

class ViewModelFactory(private val repository: Any) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ViewModelAlbum::class.java -> ViewModelAlbum(repository as RepositoryAlbumList) as T
            ViewModelAlbumList::class.java -> ViewModelAlbumList(repository as RepositoryAlbumList) as T
            ViewModelSong::class.java -> ViewModelSong(repository as RepositoryAlbumList) as T
            ViewModelScheduleList::class.java -> ViewModelScheduleList(repository as RepositoryScheduleList) as T
            ViewModelVideoList::class.java -> ViewModelVideoList(repository as RepositoryVideoList) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}