package com.beemer.unofficial.fromis_9.repository

import com.beemer.unofficial.fromis_9.api.AlbumListResponse
import com.beemer.unofficial.fromis_9.api.ApiAlbumList

class RepositoryAlbumList(private val apiAlbumList: ApiAlbumList) {
    suspend fun getAlbumList(part: String, albumName: String?, songName: String?): List<AlbumListResponse> {
        return apiAlbumList.getAlbumList(part, albumName, songName)
    }
}