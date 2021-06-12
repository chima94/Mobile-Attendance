package com.example.smartattendancesystem.data.remote

import com.example.smartattendancesystem.model.User
import com.example.smartattendancesystem.ui.intro.register.RegisterDataState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.lang.Exception

object AuthManager {

    private val auth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun Register() : Flow<RegisterDataState> = flow {
        emit(RegisterDataState.Loading)
        delay(5000)
        emit(RegisterDataState.Success(User(name = "chima")))
       /* try {
            val user = auth.createUserWithEmailAndPassword("", "").await()
        }catch (e : Exception){

        }*/
    }

}