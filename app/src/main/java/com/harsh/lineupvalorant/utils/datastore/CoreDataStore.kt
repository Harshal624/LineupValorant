package com.harsh.lineupvalorant.utils.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "core_datastore")

class CoreDataStore(appContext: Context) {
    private val coreDataStore = appContext.dataStore

    suspend fun setShouldFetch(shouldFetch: Boolean) {
        val dataStoreKey = booleanPreferencesKey(KEY_SHOULD_FETCH)
        coreDataStore.edit {
            it[dataStoreKey] = shouldFetch
        }
    }

    suspend fun shouldFetch(): Boolean? {
        val dataStoreKey = booleanPreferencesKey(KEY_SHOULD_FETCH)
        val preferences = coreDataStore.data.first()
        return preferences[dataStoreKey]
    }

    companion object {
        const val KEY_SHOULD_FETCH = "key:should_fetch"
    }
}