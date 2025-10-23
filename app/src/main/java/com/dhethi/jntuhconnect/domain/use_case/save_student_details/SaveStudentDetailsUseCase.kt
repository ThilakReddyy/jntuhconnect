package com.dhethi.jntuhconnect.domain.use_case.save_student_details

import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import com.dhethi.jntuhconnect.domain.repository.StudentDetailsRepository
import javax.inject.Inject

class SaveStudentDetailsUseCase @Inject constructor(
    private val repository: StudentDetailsRepository
) {
    suspend operator fun invoke(details: StudentDetailsEntity   ) {
        repository.saveStudentDetails(details)
    }
}
