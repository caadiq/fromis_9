package com.beemer.unofficial.fromis_9.repository

import com.beemer.unofficial.fromis_9.api.AlbumListResponse
import com.beemer.unofficial.fromis_9.api.ApiAlbumList

class RepositoryAlbum(private val apiAlbumList: ApiAlbumList) {
    suspend fun getAlbum(part: String, albumName: String): List<AlbumListResponse> {
        return apiAlbumList.getAlbumList(part, albumName)
    }
}