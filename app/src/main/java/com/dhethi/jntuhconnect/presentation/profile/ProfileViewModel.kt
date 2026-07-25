package com.dhethi.jntuhconnect.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import com.dhethi.jntuhconnect.data.repository.NotificationRepository
import com.dhethi.jntuhconnect.domain.use_case.get_all_student_details.GetAllStudentDetailsUseCase
import com.dhethi.jntuhconnect.presentation.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val notificationRepository: NotificationRepository,
    getAllDetailsUseCase: GetAllStudentDetailsUseCase
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = appPreferences.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    val notificationsEnabled: StateFlow<Boolean> = appPreferences.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _students = mutableStateOf<List<StudentDetailsEntity>>(emptyList())
    val students: State<List<StudentDetailsEntity>> = _students

    private val _notificationUpdateInProgress = MutableStateFlow(false)
    val notificationUpdateInProgress = _notificationUpdateInProgress.asStateFlow()

    private val _notificationMessage = MutableStateFlow<String?>(null)
    val notificationMessage = _notificationMessage.asStateFlow()

    init {
        viewModelScope.launch {
            getAllDetailsUseCase().collect { _students.value = it }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { appPreferences.setThemeMode(mode) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        if (_notificationUpdateInProgress.value) return
        viewModelScope.launch {
            _notificationUpdateInProgress.value = true
            _notificationMessage.value = null
            runCatching {
                notificationRepository.setResultNotificationsEnabled(enabled)
            }.onFailure {
                _notificationMessage.value = if (enabled) {
                    "Could not enable notifications. Check your connection and try again."
                } else {
                    "Could not disable notifications. Check your connection and try again."
                }
            }
            _notificationUpdateInProgress.value = false
        }
    }

    fun markNotificationPermissionRequested() {
        viewModelScope.launch {
            appPreferences.markNotificationPermissionRequested()
        }
    }

    fun onNotificationPermissionDenied() {
        _notificationMessage.value =
            "Notification permission is required. You can enable it in Android settings."
    }
}
