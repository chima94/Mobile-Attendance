package com.example.smartattendancesystem.data.remote

import com.example.smartattendancesystem.model.AttendanceModel
import com.example.smartattendancesystem.model.LocationModel
import com.example.smartattendancesystem.ui.main.attendance.AttendanceState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object AttendanceRemote {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance()}
    private val locationRef = db.collection("Location")
    private val attendanceRef = db.collection("Attendance")


    suspend fun saveLocation(locationModel: LocationModel){
        locationRef.document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(locationModel).await()
    }

    suspend fun setAttendance(attendanceModel: AttendanceModel){
        attendanceRef.document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(attendanceModel).await()
    }


    @ExperimentalCoroutinesApi
    fun getAttendance() : Flow<AttendanceState> = callbackFlow {
        val subscription = attendanceRef.addSnapshotListener { value, error ->
            if(value == null){return@addSnapshotListener}
            val result = value.toObjects(AttendanceModel::class.java)
            trySend(AttendanceState.AttendanceData(result))
        }

        awaitClose { subscription.remove() }
    }
}