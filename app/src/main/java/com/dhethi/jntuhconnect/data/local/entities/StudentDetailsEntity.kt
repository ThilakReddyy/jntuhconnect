package com.dhethi.jntuhconnect.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_details")
data class StudentDetailsEntity(
    @PrimaryKey val rollNumber: String,
    val name: String,
    val branch: String,
    val collegeCode: String,
    val semester: Int,
    val cgpa: String,
    val backlogs: Int,
    @ColumnInfo(name = "last_updated") val lastUpdated: Long = System.currentTimeMillis() // timestamp in millis

)

