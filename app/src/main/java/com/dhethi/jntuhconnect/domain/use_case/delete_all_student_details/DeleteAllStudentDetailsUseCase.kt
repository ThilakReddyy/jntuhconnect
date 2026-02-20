package com.dhethi.jntuhconnect.domain.use_case.delete_all_student_details

import com.dhethi.jntuhconnect.domain.repository.StudentDetailsRepository
import javax.inject.Inject

class DeleteAllStudentDetailsUseCase  @Inject constructor(
    private val repository: StudentDetailsRepository
) {
    suspend operator fun invoke() {
        repository.deleteAllStudents()
    }
}