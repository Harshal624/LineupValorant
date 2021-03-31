package com.harsh.lineupvalorant.utils.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "shouldfetch")

class ShouldFetchDataStore(appContext: Context) {
    private val shouldFetchDataStore = appContext.dataStore

    suspend fun setShouldFetch(shouldFetch: Boolean) {
        val dataStoreKey = booleanPreferencesKey(NAME_SHOULD_FETCH)
        shouldFetchDataStore.edit {
            it[dataStoreKey] = shouldFetch
        }
    }

    suspend fun shouldFetch(): Boolean? {
        val dataStoreKey = booleanPreferencesKey(NAME_SHOULD_FETCH)
        val preferences = shouldFetchDataStore.data.first()
        return preferences[dataStoreKey]
    }

    companion object {
        const val NAME_SHOULD_FETCH = "key:should_fetch"
    }
}