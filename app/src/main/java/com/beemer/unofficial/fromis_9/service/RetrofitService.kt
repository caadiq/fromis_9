package com.beemer.unofficial.fromis_9.service

import com.beemer.unofficial.fromis_9.BuildConfig
import com.beemer.unofficial.fromis_9.api.ApiAlbumList
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiAlbumList: ApiAlbumList by lazy {
        retrofit.create(ApiAlbumList::class.java)
    }
}