package com.example.smartattendancesystem.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class LocationStateDataStore(private val context : Context){
    private val dataStore = context.createDataStore("location_state")

    companion object{
        val STATE = preferencesKey<Boolean>("STATE")
        val REQUEST = preferencesKey<Boolean>("REQUEST")
    }

    suspend fun saveState(locationState: LocationState){
        dataStore.edit {
            it[STATE] = locationState.state
            it[REQUEST] = locationState.request
        }
    }

    val state = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }

        }
        .map {preference ->
            val state = preference[STATE] ?: false
            val request = preference[REQUEST] ?: false
            LocationState(state, request)
        }
}

data class LocationState(val state: Boolean, val request: Boolean = false)