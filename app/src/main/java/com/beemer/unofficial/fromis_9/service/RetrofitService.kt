package com.beemer.unofficial.fromis_9.service

import com.beemer.unofficial.fromis_9.BuildConfig
import com.beemer.unofficial.fromis_9.api.ApiAlbumList
import com.beemer.unofficial.fromis_9.api.ApiAppInfo
import com.beemer.unofficial.fromis_9.api.ApiIntroduction
import com.beemer.unofficial.fromis_9.api.ApiScheduleList
import com.beemer.unofficial.fromis_9.api.ApiVideoList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiScheduleList: ApiScheduleList by lazy {
        retrofit.create(ApiScheduleList::class.java)
    }

    val apiAlbumList: ApiAlbumList by lazy {
        retrofit.create(ApiAlbumList::class.java)
    }

    val apiVideoList: ApiVideoList by lazy {
        retrofit.create(ApiVideoList::class.java)
    }

    val apiIntroduction: ApiIntroduction by lazy {
        retrofit.create(ApiIntroduction::class.java)
    }

    val apiAppInfo: ApiAppInfo by lazy {
        retrofit.create(ApiAppInfo::class.java)
    }
}