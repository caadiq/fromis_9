package com.beemer.unofficial.fromis_9.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiScheduleList {
    @GET("schedulelist?")
    suspend fun getScheduleList(
        @Query("year") year: Int?,
        @Query("month") month: Int?
    ): List<ScheduleListResponse>
}

data class ScheduleListResponse(
    val id: Long,
    val dateTime: String,
    val schedule: String,
    val description: String?,
    val url: String?,
    @SerializedName("icon") val icon: Icon
)

data class Icon(
    val platform: String,
    val imageUrl: String
)