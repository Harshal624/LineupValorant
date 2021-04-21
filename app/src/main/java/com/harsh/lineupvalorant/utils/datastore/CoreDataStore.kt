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

    suspend fun setBooleanDataInDataStore(value: Boolean, dataStoreKey: String) {
        val booleanDataStoreKey = booleanPreferencesKey(dataStoreKey)
        coreDataStore.edit {
            it[booleanDataStoreKey] = value
        }
    }

    //Return false if null
    suspend fun getBooleanValeFromDataStore(dataStoreKey: String): Boolean? {
        val booleanDataStoreKey = booleanPreferencesKey(dataStoreKey)
        val preferences = coreDataStore.data.first()
        return preferences[booleanDataStoreKey]
    }

    suspend fun isFirstAppInstall(): Boolean {
        val booleanDataStoreKey = booleanPreferencesKey(KEY_IS_FIRST_INSTALL)
        val preferences = coreDataStore.data.first()
        return preferences[booleanDataStoreKey] ?: true
    }

    companion object {
        /**
         * The value associated with this key is set to true in the workmanager class
         * @see com.harsh.lineupvalorant.data.sync.PeriodicSync
         */
        const val KEY_IS_FIRST_INSTALL = "key_is_first_install"
    }
}