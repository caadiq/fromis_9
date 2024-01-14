package com.beemer.unofficial.fromis_9.repository

import com.beemer.unofficial.fromis_9.api.ApiAppInfo
import com.beemer.unofficial.fromis_9.api.Info

class RepositoryAppInfo(private val apiAppInfo: ApiAppInfo) {
    suspend fun getAppInfo(): List<Info> {
        return apiAppInfo.getAppInfo()
    }
}