package com.dhethi.jntuhconnect.domain.use_case.grace_marks

import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.common.friendlyMessage
import com.dhethi.jntuhconnect.data.remote.dto.toGraceEligibility
import com.dhethi.jntuhconnect.data.remote.dto.toGraceProofResult
import com.dhethi.jntuhconnect.domain.model.GraceEligibility
import com.dhethi.jntuhconnect.domain.model.GraceProofResult
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class CheckGraceEligibilityUseCase @Inject constructor(
    private val repository: JntuhResultsRepository
) {
    operator fun invoke(rollNumber: String): Flow<Resource<GraceEligibility>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.checkGraceEligibility(rollNumber).toGraceEligibility()))
        } catch (e: HttpException) {
            if (e.code() == 404 || e.code() == 406) {
                emit(Resource.Success(GraceEligibility(eligible = false, message = e.friendlyMessage(), backlogResult = null)))
            } else {
                emit(Resource.Error(e.friendlyMessage()))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}

class UploadGraceProofUseCase @Inject constructor(
    private val repository: JntuhResultsRepository
) {
    operator fun invoke(
        rollNumber: String,
        file: MultipartBody.Part
    ): Flow<Resource<GraceProofResult>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.uploadGraceProof(rollNumber, file).toGraceProofResult()))
        } catch (e: HttpException) {
            emit(Resource.Error(e.friendlyMessage()))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Check your internet connection"))
        }
    }
}
