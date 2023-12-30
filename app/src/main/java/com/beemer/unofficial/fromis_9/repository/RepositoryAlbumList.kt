package com.beemer.unofficial.fromis_9.repository

import android.content.Context
import com.beemer.unofficial.fromis_9.api.AlbumListResponse
import com.beemer.unofficial.fromis_9.api.ApiAlbumList
import com.beemer.unofficial.fromis_9.datastore.Preferences

class RepositoryAlbumList(private val apiAlbumList: ApiAlbumList, private val context: Context) {
    suspend fun getAlbumList(part: String, albumName: String?, songName: String?): List<AlbumListResponse> {
        return apiAlbumList.getAlbumList(part, albumName, songName)
    }

    suspend fun setSortBy(sortBy: String) {
        Preferences.setSortBy(context, sortBy)
    }

    suspend fun getSortBy(): String {
        return Preferences.getSortBy(context)
    }

    suspend fun setIsAscending(isAscending: Boolean) {
        Preferences.setIsAscending(context, isAscending)
    }

    suspend fun getIsAscending(): Boolean {
        return Preferences.getIsAscending(context)
    }
}