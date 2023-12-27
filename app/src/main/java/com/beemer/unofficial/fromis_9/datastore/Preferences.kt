package com.beemer.unofficial.fromis_9.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "MyPrefs")

object Preferences {
    private val SORT_BY = stringPreferencesKey("sortBy")
    private val IS_ASCENDING = booleanPreferencesKey("isAscending")

    suspend fun setSortBy(context: Context, sortBy: String) {
        context.dataStore.edit { preferences ->
            preferences[SORT_BY] = sortBy
        }
    }

    suspend fun getSortBy(context: Context): String {
        return context.dataStore.data.map { preferences ->
            preferences[SORT_BY] ?: "release"
        }.first()
    }

    suspend fun setIsAscending(context: Context, isAscending: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_ASCENDING] = isAscending
        }
    }

    suspend fun getIsAscending(context: Context): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[IS_ASCENDING] ?: false
        }.first()
    }
}