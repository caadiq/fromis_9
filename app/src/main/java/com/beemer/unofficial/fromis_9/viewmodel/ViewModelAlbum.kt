package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.data.DataTrackList
import com.beemer.unofficial.fromis_9.repository.RepositoryAlbum
import com.beemer.unofficial.fromis_9.utils.Event
import kotlinx.coroutines.launch

class ViewModelFactoryAlbum(private val repository: RepositoryAlbum) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelAlbum::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewModelAlbum(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ViewModelAlbum(private val repository: RepositoryAlbum) : ViewModel() {
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _trackList = MutableLiveData<List<DataTrackList>>()
    val trackList: LiveData<List<DataTrackList>> = _trackList

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun getAlbum(albumName: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAlbum("album,description,tracklist", albumName)
                val albumResponse = response.firstOrNull()
                albumResponse?.albumDescription?.let {
                    _description.value = it.description
                }
                val colorMain = albumResponse?.colorMain ?: ""

                _trackList.value = albumResponse?.trackList?.song?.map { song ->
                    DataTrackList(
                        colorMain = colorMain,
                        trackNumber = song.trackNumber,
                        songName = song.songName,
                        songLength = song.songLength,
                        titleTrack = song.titleTrack,
                    )
                }
            } catch (_: Exception) {
                _errorMessage.value = Event("앨범 정보를 불러오지 못했습니다.")
            }
        }
    }
}