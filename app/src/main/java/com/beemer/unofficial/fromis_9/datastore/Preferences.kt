package com.beemer.unofficial.fromis_9.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "MyPrefs")

object Preferences {
    private val SORT_BY = intPreferencesKey("sortBy")
    private val IS_ASCENDING = booleanPreferencesKey("isAscending")

    suspend fun setSortBy(context: Context, sortBy: Int) {
        context.dataStore.edit { preferences ->
            preferences[SORT_BY] = sortBy
        }
    }

    suspend fun getSortBy(context: Context): Int {
        return context.dataStore.data.map { preferences ->
            preferences[SORT_BY] ?: 0
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