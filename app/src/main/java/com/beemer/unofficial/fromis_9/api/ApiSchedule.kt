package com.beemer.unofficial.fromis_9.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiSchedule {
    @GET("schedulelist?")
    suspend fun getSchedules(
        @Query("year") year: Int?,
        @Query("month") month: Int?
    ): List<ScheduleResponse>
}

data class ScheduleResponse(
    val id: Long,
    val dateTime: String,
    val schedule: String,
    val description: String?,
    @SerializedName("icon") val icon: Icon
)

data class Icon(
    val platform: String,
    val imageUrl: String
)