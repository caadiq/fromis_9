package com.beemer.unofficial.fromis_9.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiAlbumList {
    @GET("albumlist")
    suspend fun getAlbumList(
        @Query("part") part: String,
        @Query("albumname") albumName: String?,
        @Query("songname") songName: String?,
    ): List<AlbumListResponse>
}

data class AlbumListResponse(
    val albumName: String?,
    val albumType: String?,
    val releaseDate: String?,
    val colorMain: String?,
    val colorPrimary: String?,
    val colorSecondary: String?,
    @SerializedName("albumArt") val albumArt: AlbumArt?,
    @SerializedName("albumDescription") val albumDescription: AlbumDescription?,
    @SerializedName("trackList") val trackList: TrackListResponse?,
    @SerializedName("photoList") val photoList: PhotoListResponse?,
)

data class TrackListResponse(
    @SerializedName("song") val song: List<Song>,
)

data class PhotoListResponse(
    @SerializedName("photo") val photo: List<Photo>,
)

data class AlbumArt(
    val albumName: String,
    val imageUrl: String
)

data class AlbumDescription(
    val albumName: String,
    val description: String
)

data class Song(
    val songName: String,
    val albumName: String,
    val lyricist: String,
    val composer: String,
    val arranger: String,
    val lyrics: String,
    val songLength: String,
    val trackNumber: Int,
    val titleTrack: Boolean,
    val videoId: String
)

data class Photo(
    val albumName: String,
    val concept: String,
    val imageUrl: String
)