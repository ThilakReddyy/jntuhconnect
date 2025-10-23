package com.dhethi.jntuhconnect.domain.use_case.get_fcm_token

import com.dhethi.jntuhconnect.data.repository.NotificationRepository
import javax.inject.Inject

class GetFcmTokenUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(): String? = repository.fetchFcmToken()
}
