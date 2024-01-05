package com.beemer.unofficial.fromis_9.repository

import com.beemer.unofficial.fromis_9.api.ApiVideoList
import com.beemer.unofficial.fromis_9.api.VideoListResponse

class RepositoryVideoList(private val apiVideo: ApiVideoList) {
    suspend fun getVideoList(type: String?, limit: Int?, search: String?, page: Int?): VideoListResponse {
        return apiVideo.getVideoList(type, limit, search, page)
    }
}