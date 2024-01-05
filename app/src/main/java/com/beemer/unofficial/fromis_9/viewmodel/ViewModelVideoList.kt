package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.beemer.unofficial.fromis_9.data.DataVideoList
import com.beemer.unofficial.fromis_9.repository.RepositoryVideoList
import com.beemer.unofficial.fromis_9.utils.Event
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ViewModelVideoList(private val repository: RepositoryVideoList) : ViewModel() {
    private val _videoList = MutableLiveData<List<DataVideoList>>()
    val videoList: LiveData<List<DataVideoList>> = _videoList

    private val _videoType = MutableLiveData<String>()
    val videoType: LiveData<String> = _videoType

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    private val _nextPage = MutableLiveData<Int>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun updateVideoType(videoType: String) {
        viewModelScope.launch {
            _videoType.value = videoType
        }
    }

    fun getVideoList(recyclerView: RecyclerView, type: String, search: String?) {
        viewModelScope.launch {
            try {
                val searchQuery = search ?: _searchQuery.value
                val response = repository.getVideoList(type, 20, searchQuery, null)
                val videoList = response.videos.map {
                    val publishedAt = LocalDateTime.parse(it.publishedAt, DateTimeFormatter.ISO_DATE_TIME)
                    val publishedAtFormatted = publishedAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

                    DataVideoList(
                        videoId = it.videoId,
                        publishedAt = publishedAtFormatted,
                        title = it.title,
                        description = it.description,
                        thumbnailUrl = it.thumbnailUrl
                    )
                }

                _videoList.value = videoList
                _nextPage.value = response.page.nextPage
                _searchQuery.value = searchQuery
            } catch (e: Exception) {
                _errorMessage.value = Event("영상 목록을 불러오지 못했습니다.")
            } finally {
                recyclerView.scrollToPosition(0)
            }
        }
    }

    fun getNextVideoList() {
        val currentPage = _nextPage.value ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = repository.getVideoList(videoType.value, 20, _searchQuery.value, currentPage)
                val nextVideoList = response.videos.map {
                    val publishedAt = LocalDateTime.parse(it.publishedAt, DateTimeFormatter.ISO_DATE_TIME)
                    val publishedAtFormatted = publishedAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))

                    DataVideoList(
                        videoId = it.videoId,
                        publishedAt = publishedAtFormatted,
                        title = it.title,
                        description = it.description,
                        thumbnailUrl = it.thumbnailUrl
                    )
                }

                val updatedList = _videoList.value.orEmpty() + nextVideoList
                _videoList.postValue(updatedList)
                _nextPage.value = response.page.nextPage
            } catch (e: Exception) {
                _errorMessage.value = Event("영상 목록을 불러오지 못했습니다.")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetSearchQuery() {
        _searchQuery.value = null
    }
}