package com.example.smartattendancesystem.data.repositories

import android.net.Uri
import com.example.smartattendancesystem.data.remote.AuthManager
import com.example.smartattendancesystem.model.User
import com.example.smartattendancesystem.ui.intro.login.LoginState
import com.example.smartattendancesystem.ui.intro.register.RegisterAction
import com.example.smartattendancesystem.ui.intro.register.RegisterDataState
import com.example.smartattendancesystem.ui.main.facerecognition.CameraState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import timber.log.Timber

class AuthRepository(
    private val authManager : AuthManager,
    private val userManagerDataStore: UserManagerDataStore,
    private val defaultDispatcher: CoroutineDispatcher

) {

    val userData : Flow<User> = userManagerDataStore.userData
    val signInUser  = authManager.user



    fun register(userData: User) : Flow<RegisterDataState> =
        authManager.Register(userData)
            .onEach { user ->  if(user is RegisterDataState.Success) storeData(user = user.user)}
            .flowOn(defaultDispatcher)
            .catch { exception ->
                emit(RegisterDataState.Error(exception.localizedMessage!!))
            }


    private suspend fun storeData(user : User){
        authManager.saveUser(user)
        saveUserLocally(user = user)
    }

    private suspend fun saveUserLocally(user: User){
        userManagerDataStore.storeUserData(user)
    }


    private suspend fun saveLoginUser(userId: String){
        val user = authManager.getUser(userId)
        saveUserLocally(user!!)
    }

    fun login(email : String, password : String) : Flow<LoginState> =
        authManager.login(email, password)
            .onEach { userId -> if(userId is LoginState.Success) saveLoginUser(userId.userId) }
            .flowOn(defaultDispatcher)
            .catch { emit(LoginState.Error(it.localizedMessage!!)) }


    fun verifyUserIdNum(userIdNum : String) : Flow<List<User>> =
        authManager.verifyUserIdNumber(userIdNum)



    @ExperimentalCoroutinesApi
    fun uploadImage(imageUri : Uri, user: User) : Flow<CameraState> =
        authManager.uploadImage(imageUri).onEach { downloadUri ->
            if(downloadUri is CameraState.Success){
                user.imageUri = downloadUri.imageUri
                storeData(user)
            }

        }
            .flowOn(defaultDispatcher)
            .catch { emit(CameraState.Error(it.localizedMessage!!)) }



    fun logout(){
        authManager.logout()
    }
}