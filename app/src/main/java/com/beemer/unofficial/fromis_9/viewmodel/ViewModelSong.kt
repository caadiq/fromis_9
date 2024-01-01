package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.repository.RepositoryAlbumList
import com.beemer.unofficial.fromis_9.utils.Event
import kotlinx.coroutines.launch

class ViewModelSong(private val repository: RepositoryAlbumList) : ViewModel() {
    private val _lyricist = MutableLiveData<String>()
    val lyricist: LiveData<String> = _lyricist

    private val _composer = MutableLiveData<String>()
    val composer: LiveData<String> = _composer

    private val _arranger = MutableLiveData<String>()
    val arranger: LiveData<String> = _arranger

    private val _lyrics = MutableLiveData<String>()
    val lyrics: LiveData<String> = _lyrics

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun getSong(albumName: String, songName: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAlbumList("tracklist", albumName, songName)
                val albumResponse = response.firstOrNull()

                albumResponse?.trackList?.song?.firstOrNull()?.let {
                    _lyricist.value = it.lyricist
                    _composer.value = it.composer
                    _arranger.value = it.arranger
                    _lyrics.value = it.lyrics
                }
            } catch (_: Exception) {
                _errorMessage.value = Event("노래 정보를 불러오지 못했습니다.")
            }
        }
    }
}