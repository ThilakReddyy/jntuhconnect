package com.dhethi.jntuhconnect.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.domain.use_case.get_fcm_token.GetFcmTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getFcmTokenUseCase: GetFcmTokenUseCase
) : ViewModel() {

    private val _fcmToken = MutableStateFlow<String?>(null)
    val fcmToken: StateFlow<String?> = _fcmToken

    fun loadFcmToken() {
        viewModelScope.launch {
            val token = getFcmTokenUseCase()
            _fcmToken.value = token
        }
    }
}
