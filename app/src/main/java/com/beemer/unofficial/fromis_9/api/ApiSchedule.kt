package com.beemer.unofficial.fromis_9.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSchedule {
    @GET("schedules?")
    suspend fun getSchedules(
        @Query("year") year: Int?,
        @Query("month") month: Int?
    ): List<ScheduleResponse>
}

data class ScheduleResponse(
    val id: Long,
    val date: String,
    val title: String,
    val description: String?,
    @SerializedName("platformIcon") val platformIcon: PlatformIcon
)

data class PlatformIcon(
    val platform: String,
    val imageUrl: String
)