package com.example.smartattendancesystem.data.remote

import android.net.Uri
import com.example.smartattendancesystem.model.User
import com.example.smartattendancesystem.ui.intro.login.LoginState
import com.example.smartattendancesystem.ui.intro.register.RegisterDataState
import com.example.smartattendancesystem.ui.main.facerecognition.CameraState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.lang.Exception

object AuthManager {

    private val auth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}
    private val userRef = db.collection("User")
    private val storageRef = FirebaseStorage.getInstance().reference

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



    @ExperimentalCoroutinesApi
    fun uploadImage(imageUri : Uri) : Flow<CameraState> = callbackFlow {
        trySend(CameraState.Loading)
        val imageRef =
            storageRef.child("userImages/"+FirebaseAuth.getInstance().currentUser!!.uid
            +"/"+FirebaseAuth.getInstance().currentUser!!.uid+"."+"jpeg")

        val snapshot = imageRef.putFile(imageUri).addOnSuccessListener {
            imageRef.downloadUrl.addOnCompleteListener {
                trySend(CameraState.Success(it.result.toString()))
            }
        }
            .addOnFailureListener{
                trySend(CameraState.Error(it.localizedMessage!!))
            }
            awaitClose { snapshot.cancel() }

    }


     fun logout(){
        auth.signOut()
    }
}