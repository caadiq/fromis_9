package com.beemer.unofficial.fromis_9.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiAlbumList {
    @GET("albumlist")
    suspend fun getAlbumList(
        @Query("part") part: String
    ): List<AlbumListResponse>
}

data class AlbumListResponse(
    val albumName: String?,
    val albumType: String?,
    val releaseDate: String?,
    val colorPrimary: String?,
    val colorSecondary: String?,
    @SerializedName("albumArt") val albumArt: AlbumArt?,
    @SerializedName("albumDescription") val albumDescription: AlbumDescription?
)

data class AlbumArt(
    val albumName: String,
    val imageUrl: String
)

data class AlbumDescription(
    val albumName: String,
    val description: String
)