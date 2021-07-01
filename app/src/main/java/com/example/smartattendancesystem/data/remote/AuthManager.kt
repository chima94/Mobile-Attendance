package com.example.smartattendancesystem.data.remote

import com.example.smartattendancesystem.model.User
import com.example.smartattendancesystem.ui.intro.login.LoginState
import com.example.smartattendancesystem.ui.intro.register.RegisterDataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception

object AuthManager {

    private val auth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}
    private val userRef = db.collection("User")

    val user = auth.currentUser?.uid

    fun Register(userData: User) : Flow<RegisterDataState> = flow {
        emit(RegisterDataState.Loading)
        val user = auth.createUserWithEmailAndPassword(userData.email, userData.password).await()
        userData.userId = user.user!!.uid
        emit(RegisterDataState.Success(userData))

    }

    suspend fun saveUser(userData : User){
        userRef.document(userData.userId).set(userData).await()
    }


    fun login(email : String, password : String) : Flow<LoginState> = flow{
        emit(LoginState.Loading)
        val user = auth.signInWithEmailAndPassword(email, password).await()
        emit(LoginState.Success(user.user!!.uid))
    }


    suspend fun getUser(userId : String) : User?{
        val user = userRef.document(userId).get().await().toObject(User::class.java)
        return user
    }

    fun verifyUserIdNumber(userIdNum : String) : Flow<List<User>> = flow {
        val user = userRef.whereEqualTo("userIdNum", userIdNum).get().await()
            .toObjects(User::class.java)
        emit(user)
    }


     fun logout(){
        auth.signOut()
    }
}