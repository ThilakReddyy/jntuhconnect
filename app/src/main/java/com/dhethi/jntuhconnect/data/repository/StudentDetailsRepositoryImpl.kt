package com.dhethi.jntuhconnect.data.repository

import com.dhethi.jntuhconnect.data.local.dao.StudentDetailsDao
import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import com.dhethi.jntuhconnect.domain.repository.StudentDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StudentDetailsRepositoryImpl @Inject constructor(
    private val dao: StudentDetailsDao
) : StudentDetailsRepository {

    override suspend fun saveStudentDetails(details: StudentDetailsEntity) {
        dao.insertDetails(details)
    }

    override suspend fun getStudentDetails(rollNumber: String): StudentDetailsEntity? {
        return dao.getDetails(rollNumber)
    }

    override suspend fun getAllStudentDetails(): Flow<List<StudentDetailsEntity>> {
        return dao.getAllDetails()
    }

    override suspend fun deleteStudentDetails(rollNumber: String) {
        dao.deleteDetails(rollNumber)
    }

    override suspend fun deleteAllStudents() {
        dao.deleteAllDetails()
    }
}
