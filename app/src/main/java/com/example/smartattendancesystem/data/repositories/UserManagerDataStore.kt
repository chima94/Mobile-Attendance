package com.example.smartattendancesystem.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore

import com.example.smartattendancesystem.model.User
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserManagerDataStore(val context: Context) {

    private val dataStore = context.createDataStore("User_pref")

    companion object{
        val USER_NAME = preferencesKey<String>("USER_NAME")
        val USER_EMAIL = preferencesKey<String>("USER_EMAIL")
        val USER_ID = preferencesKey<String>("USER_ID")
        val USER_ID_NUM = preferencesKey<String>("USER_ID_NUM")
        val USER_TYPE = preferencesKey<String>("USER_TYPE")
        val USER_IMAGE_URI = preferencesKey<String>("USER_IMAGE_URI")
        val SCHOOL = preferencesKey<String>("SCHOOL")
    }

    suspend fun storeUserData(user: User){
        dataStore.edit {
            it[USER_NAME] = user.name
            it[USER_EMAIL] = user.email
            it[USER_ID] = user.userId
            it[USER_ID_NUM] = user.userIdNum
            it[USER_TYPE] = user.userType
            it[USER_IMAGE_URI] = user.imageUri
            it[SCHOOL] = user.school
        }
    }

    val userData = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map {preferences ->
            val name = preferences[USER_NAME] ?: ""
            val email = preferences[USER_EMAIL] ?: ""
            val userId = preferences[USER_ID] ?: ""
            val userIdNum = preferences[USER_ID_NUM] ?: ""
            val userType  = preferences[USER_TYPE] ?: ""
            val imageUri = preferences[USER_IMAGE_URI] ?: ""
            val school = preferences[SCHOOL] ?: ""

            User(
                email = email,
                name = name,
                userId = userId,
                userIdNum = userIdNum,
                userType = userType,
                imageUri = imageUri,
                school = school
            )
        }

}