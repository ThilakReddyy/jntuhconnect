package com.dhethi.jntuhconnect.domain.use_case.get_all_student_details

import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import com.dhethi.jntuhconnect.domain.repository.StudentDetailsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllStudentDetailsUseCase @Inject constructor(
    private val repository: StudentDetailsRepository
) {
    suspend operator fun invoke(): Flow<List<StudentDetailsEntity>> {
        return repository.getAllStudentDetails()
    }
}
