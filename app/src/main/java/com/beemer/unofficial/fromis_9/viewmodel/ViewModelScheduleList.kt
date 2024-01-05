package com.beemer.unofficial.fromis_9.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.beemer.unofficial.fromis_9.data.DataSchedule
import com.beemer.unofficial.fromis_9.repository.RepositoryScheduleList
import com.beemer.unofficial.fromis_9.utils.Event
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ViewModelScheduleList(private val repository: RepositoryScheduleList) : ViewModel() {
    private val _scheduleList = MutableLiveData<Map<LocalDate, List<DataSchedule>>>()
    val scheduleList: LiveData<Map<LocalDate, List<DataSchedule>>> = _scheduleList

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    fun getScheduleList(year: Int, month: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getScheduleList(year, month)
                val scheduleDataMap = mutableMapOf<LocalDate, MutableList<DataSchedule>>()

                response.forEach {
                    val dateTime = LocalDateTime.parse(it.dateTime, DateTimeFormatter.ISO_DATE_TIME)
                    val time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))

                    val dataSchedule = DataSchedule(
                        time = time,
                        schedule = it.schedule,
                        description = it.description,
                        image = it.icon.imageUrl
                    )

                    val scheduleList = scheduleDataMap.getOrDefault(dateTime.toLocalDate(), mutableListOf()).toMutableList()
                    scheduleList.add(dataSchedule)
                    scheduleDataMap[dateTime.toLocalDate()] = scheduleList
                }

                _scheduleList.value = scheduleDataMap
            } catch (_: Exception) {
                _errorMessage.value = Event("일정 목록을 불러오지 못했습니다.")
            }
        }
    }
}