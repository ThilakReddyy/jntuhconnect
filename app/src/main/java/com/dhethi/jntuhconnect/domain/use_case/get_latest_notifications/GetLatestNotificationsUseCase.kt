package com.dhethi.jntuhconnect.domain.use_case.get_latest_notifications

import android.util.Log
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.data.remote.dto.toLatestNotification
import com.dhethi.jntuhconnect.domain.model.LatestNotification
import com.dhethi.jntuhconnect.domain.repository.JntuhResultsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class GetLatestNotificationsUseCase @Inject constructor(
    private val repository: JntuhResultsRepository
) {
    companion object {
        private const val TAG = "GetLatestNotifications"
    }

    operator fun invoke(
        page: Int,
        category: String,
        regulation: String = "",
        degree: String = "",
        year: String = "",
        title: String = ""
    ): Flow<Resource<List<LatestNotification>>> =
        flow {
            try {
                emit(Resource.Loading())
                val response =
                    repository.getNotifications(page, category, regulation, degree, year, title)
                val latestNotifications = response.map { it.toLatestNotification() }
                emit(Resource.Success(latestNotifications))

            } catch (e: HttpException) {
                Log.e(TAG, "HttpException: ${e.localizedMessage}", e)
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: IOException) {
                Log.e(TAG, "IOException: ${e.localizedMessage}", e)
                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected exception: ${e.localizedMessage}", e)
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
}
