package com.dhethi.jntuhconnect.domain.repository

import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import kotlinx.coroutines.flow.Flow

interface StudentDetailsRepository {
    suspend fun saveStudentDetails(details: StudentDetailsEntity)
    suspend fun getStudentDetails(rollNumber: String): StudentDetailsEntity?
    suspend fun getAllStudentDetails(): Flow<List<StudentDetailsEntity>>
    suspend fun deleteStudentDetails(rollNumber: String)
    suspend fun deleteAllStudents()
}
