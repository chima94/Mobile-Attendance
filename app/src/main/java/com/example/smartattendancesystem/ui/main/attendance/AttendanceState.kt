package com.example.smartattendancesystem.ui.main.attendance

import com.example.smartattendancesystem.model.AttendanceModel

sealed class AttendanceState{
    data class AttendanceData(var classes: MutableList<AttendanceModel>) : AttendanceState()
    object Loading : AttendanceState()
    object Nothing : AttendanceState()
}
