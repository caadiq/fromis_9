package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.data.DataBannerImage
import com.beemer.unofficial.fromis_9.data.DataMembers
import com.beemer.unofficial.fromis_9.repository.RepositoryIntroduction
import com.beemer.unofficial.fromis_9.utils.Event
import kotlinx.coroutines.launch

class ViewModelIntroduction(private val repository: RepositoryIntroduction) : ViewModel() {
    private val _bannerImage = MutableLiveData<List<DataBannerImage>>()
    val bannerImage: LiveData<List<DataBannerImage>> = _bannerImage

    private val _members = MutableLiveData<List<DataMembers>>()
    val members: LiveData<List<DataMembers>> = _members

    private val _debutDate = MutableLiveData<String>()
    val debutDate: LiveData<String> = _debutDate

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun getIntroduction() {
        viewModelScope.launch {
            try {
                val response = repository.getIntroduction()
                _bannerImage.value = response.bannerImages.map {
                    DataBannerImage(imageUrl = it.imageUrl)
                }
                _debutDate.value = response.debutDate.debutDate
                _members.value = response.members.map { member ->
                    DataMembers(
                        name = member.name,
                        imageUrl = member.imageUrl,
                        birth = member.birth,
                        position = member.position,
                        blood = member.blood
                    )
                }
            } catch (_: Exception) {
                _errorMessage.value = Event("프로미스나인 정보를 불러오지 못했습니다.")
            }
        }
    }
}