package com.beemer.unofficial.fromis_9.repository

import com.beemer.unofficial.fromis_9.api.ApiScheduleList
import com.beemer.unofficial.fromis_9.api.ScheduleListResponse

class RepositoryScheduleList(private val apiScheduleList: ApiScheduleList) {
    suspend fun getScheduleList(year: Int, month: Int): List<ScheduleListResponse> {
        return apiScheduleList.getScheduleList(year, month)
    }
}