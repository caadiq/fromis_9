package com.beemer.unofficial.fromis_9.model.api

import com.beemer.unofficial.fromis_9.model.dto.ScheduleListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ScheduleApi {
    @GET("/api/fromis9/schedules")
    fun getScheduleList(
        @Query("year") year: Int?,
        @Query("month") month: Int?,
    ): Call<List<ScheduleListDto>>
}