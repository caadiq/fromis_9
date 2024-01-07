package com.beemer.unofficial.fromis_9.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiVideoList {
    @GET("videolist?")
    suspend fun getVideoList(
        @Query("type") type: String?,
        @Query("limit") limit: Int?,
        @Query("search") search: String?,
        @Query("page") page: Int?
    ): VideoListResponse
}

data class VideoListResponse(
    val page: Page,
    val videos: List<Videos>
)

data class Page(
    val currentPage: Int,
    val nextPage: Int?
)

data class Videos(
    val videoId: String,
    val publishedAt: String,
    val title: String,
    val thumbnailUrl: String
)