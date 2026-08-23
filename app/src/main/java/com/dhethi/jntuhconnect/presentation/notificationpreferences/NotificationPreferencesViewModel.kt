package com.dhethi.jntuhconnect.presentation.notificationpreferences

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import com.dhethi.jntuhconnect.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationPreferencesViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _state = mutableStateOf(NotificationPreferencesState())
    val state: State<NotificationPreferencesState> = _state

    init {
        viewModelScope.launch {
            // Instant local value first, then reconcile with the server in case
            // this device's local DataStore was wiped (e.g. reinstall) but the
            // backend still remembers a saved preference for this device id.
            _state.value = _state.value.copy(
                notificationsEnabled = appPreferences.notificationsEnabled.first(),
                degrees = appPreferences.notificationPrefDegrees.first(),
                regulations = appPreferences.notificationPrefRegulations.first(),
                loading = false
            )
            runCatching { notificationRepository.getNotificationTopicPreference() }
                .onSuccess { response ->
                    _state.value = _state.value.copy(
                        degrees = response.degrees.toSet(),
                        regulations = response.regulations.toSet()
                    )
                }
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        if (_state.value.updatingEnabled) return
        viewModelScope.launch {
            _state.value = _state.value.copy(updatingEnabled = true, error = null)
            runCatching {
                notificationRepository.setResultNotificationsEnabled(enabled)
            }.onSuccess {
                _state.value = _state.value.copy(notificationsEnabled = enabled)
            }.onFailure {
                _state.value = _state.value.copy(
                    error = if (enabled) {
                        "Could not enable notifications. Check your connection and try again."
                    } else {
                        "Could not disable notifications. Check your connection and try again."
                    }
                )
            }
            _state.value = _state.value.copy(updatingEnabled = false)
        }
    }

    fun markNotificationPermissionRequested() {
        viewModelScope.launch { appPreferences.markNotificationPermissionRequested() }
    }

    fun onNotificationPermissionDenied() {
        _state.value = _state.value.copy(
            error = "Notification permission is required. You can enable it in Android settings."
        )
    }

    fun toggleDegree(code: String) {
        val current = _state.value.degrees
        _state.value = _state.value.copy(
            degrees = if (code in current) current - code else current + code,
            error = null
        )
    }

    fun toggleRegulation(code: String) {
        val current = _state.value.regulations
        _state.value = _state.value.copy(
            regulations = if (code in current) current - code else current + code,
            error = null
        )
    }

    fun save() {
        if (_state.value.saving) return
        val degrees = _state.value.degrees
        val regulations = _state.value.regulations
        viewModelScope.launch {
            _state.value = _state.value.copy(saving = true, error = null)
            runCatching {
                notificationRepository.saveNotificationTopicPreference(degrees, regulations)
            }.onSuccess {
                // Saving a filter implicitly turns alerts on if they were off.
                _state.value = _state.value.copy(notificationsEnabled = true)
            }.onFailure {
                _state.value = _state.value.copy(
                    error = "Could not save preferences. Check your connection and try again."
                )
            }
            _state.value = _state.value.copy(saving = false)
        }
    }

    fun resetToAll() {
        if (_state.value.saving) return
        viewModelScope.launch {
            _state.value = _state.value.copy(saving = true, error = null)
            runCatching {
                notificationRepository.clearNotificationTopicPreference()
            }.onSuccess {
                _state.value = _state.value.copy(degrees = emptySet(), regulations = emptySet())
            }.onFailure {
                _state.value = _state.value.copy(
                    error = "Could not reset preferences. Check your connection and try again."
                )
            }
            _state.value = _state.value.copy(saving = false)
        }
    }
}
