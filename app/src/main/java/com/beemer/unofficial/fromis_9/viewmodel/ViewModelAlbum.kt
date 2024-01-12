package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.data.DataPhotoList
import com.beemer.unofficial.fromis_9.data.DataTrackList
import com.beemer.unofficial.fromis_9.repository.RepositoryAlbumList
import com.beemer.unofficial.fromis_9.utils.Event
import kotlinx.coroutines.launch

class ViewModelAlbum(private val repository: RepositoryAlbumList) : ViewModel() {
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _trackList = MutableLiveData<List<DataTrackList>>()
    val trackList: LiveData<List<DataTrackList>> = _trackList

    private val _photoList = MutableLiveData<List<DataPhotoList>>()
    val photoList: LiveData<List<DataPhotoList>> = _photoList

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun getAlbum(albumName: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAlbumList("album,description,tracklist,photolist", albumName, null)
                val albumResponse = response.firstOrNull()
                albumResponse?.albumDescription?.let {
                    _description.value = it.description
                }
                val colorMain = albumResponse?.colorMain ?: ""
                val colorPrimary = albumResponse?.colorPrimary ?: ""
                val colorSecondary = albumResponse?.colorSecondary ?: ""

                _trackList.value = albumResponse?.trackList?.song?.map { song ->
                    DataTrackList(
                        albumName = albumName,
                        colorMain = colorMain,
                        colorPrimary = colorPrimary,
                        colorSecondary = colorSecondary,
                        trackNumber = song.trackNumber,
                        songName = song.songName,
                        songLength = song.songLength,
                        titleTrack = song.titleTrack,
                        videoId = song.videoId
                    )
                }

                _photoList.value = albumResponse?.photoList?.photo?.map { photo ->
                    DataPhotoList(
                        concept = photo.concept,
                        imageUrl = photo.imageUrl
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = Event("앨범 정보를 불러오지 못했습니다.")
            }
        }
    }
}