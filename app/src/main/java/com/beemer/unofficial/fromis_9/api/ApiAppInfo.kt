package com.beemer.unofficial.fromis_9.api

import retrofit2.http.GET

interface ApiAppInfo {
    @GET("appinfo")
    suspend fun getAppInfo(): List<Info>
}

data class Info(
    val version: String,
    val release: String,
    val changelog: String
)