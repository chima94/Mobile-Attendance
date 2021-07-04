package com.example.smartattendancesystem.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "classModel")
data class ClassModel @JvmOverloads constructor(

    @ColumnInfo(name = "courseTitle") var courseTitle : String = "",
    @ColumnInfo(name = "classState") var classState : Boolean = false,
    @ColumnInfo(name = "userId") var userId : String = "",
    @PrimaryKey @ColumnInfo(name = "entryId") var id : String = courseTitle

)