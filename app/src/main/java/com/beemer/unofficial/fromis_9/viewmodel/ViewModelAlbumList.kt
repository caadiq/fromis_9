package com.beemer.unofficial.fromis_9.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.data.DataAlbumList
import com.beemer.unofficial.fromis_9.repository.RepositoryAlbumList
import com.beemer.unofficial.fromis_9.utils.Event
import kotlinx.coroutines.launch

class ViewModelFactoryAlbumList(private val repository: RepositoryAlbumList) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelAlbumList::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelAlbumList(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ViewModelAlbumList(private val repository: RepositoryAlbumList) : ViewModel() {
    private val _albumList = MutableLiveData<List<DataAlbumList>>()
    val albumList: LiveData<List<DataAlbumList>> = _albumList

    private val _sortBy = MutableLiveData<String>()
    val sortBy: LiveData<String> = _sortBy

    private val _isAscending = MutableLiveData<Boolean>()
    val isAscending: LiveData<Boolean> = _isAscending

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _sortBy.value = repository.getSortBy()
            _isAscending.value = repository.getIsAscending()
        }
    }

    fun updateSortBy(sortBy: String) {
        viewModelScope.launch {
            repository.setSortBy(sortBy)
            _sortBy.value = sortBy
            _albumList.value?.let {
                sortAlbumList()
            }
        }
    }

    fun updateIsAscending(isAscending: Boolean) {
        viewModelScope.launch {
            repository.setIsAscending(isAscending)
            _isAscending.value = isAscending
            _albumList.value?.let {
                sortAlbumList()
            }
        }
    }

    fun getAlbumList() {
        Log.d("ViewModelAlbumList", "getAlbumList: called")
        viewModelScope.launch {
            try {
                val response = repository.getAlbumList("album,art", null)
                _albumList.value = response.map {
                    DataAlbumList(
                        albumName = it.albumName ?: "",
                        albumType = it.albumType ?: "",
                        releaseDate = it.releaseDate ?: "",
                        colorMain = it.colorMain ?: "",
                        colorPrimary = it.colorPrimary ?: "",
                        colorSecondary = it.colorSecondary ?: "",
                        albumArt = it.albumArt?.imageUrl ?: ""
                    )
                }
                sortAlbumList()
            } catch (e: Exception) {
                _errorMessage.value = Event("앨범 목록을 불러오지 못했습니다.")
            }
        }
    }

    private fun sortAlbumList() {
        val currentSortBy = _sortBy.value ?: "release"
        val currentIsAscending = _isAscending.value ?: true

        val sortedList = when (currentSortBy) {
            "release" -> _albumList.value?.sortedBy { it.releaseDate }
            "title" -> _albumList.value?.sortedBy { it.albumName.lowercase() }
            "type" -> _albumList.value?.sortedWith(compareBy<DataAlbumList> { it.albumType }.thenBy { it.albumName.lowercase() })
            else -> _albumList.value
        }

        _albumList.value = if (currentIsAscending) sortedList else sortedList?.reversed()
    }
}