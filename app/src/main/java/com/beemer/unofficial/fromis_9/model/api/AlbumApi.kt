package com.beemer.unofficial.fromis_9.model.api

import com.beemer.unofficial.fromis_9.model.dto.AlbumDetailsDto
import com.beemer.unofficial.fromis_9.model.dto.AlbumListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AlbumApi {
    @GET("/api/fromis9/album/list")
    fun getAlbumList(): Call<List<AlbumListDto>>

    @GET("/api/fromis9/album/details")
    fun getAlbumDetails(@Query("album") album: String): Call<AlbumDetailsDto>
}