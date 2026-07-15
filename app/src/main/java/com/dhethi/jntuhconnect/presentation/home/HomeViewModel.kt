package com.dhethi.jntuhconnect.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.domain.use_case.delete_all_student_details.DeleteAllStudentDetailsUseCase
import com.dhethi.jntuhconnect.domain.use_case.get_all_student_details.GetAllStudentDetailsUseCase
import com.dhethi.jntuhconnect.domain.use_case.get_latest_notifications.GetLatestNotificationsUseCase
import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import com.dhethi.jntuhconnect.domain.model.RecentDocument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllDetailsUseCase: GetAllStudentDetailsUseCase,
    private val deleteAllStudentDetailsUseCase: DeleteAllStudentDetailsUseCase,
    private val getLatestNotificationsUseCase: GetLatestNotificationsUseCase,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state
    private var documentExpiryJob: Job? = null

    init {
        loadStudents()
        loadRecentDocuments()
        loadLatestUpdates()
    }

    private fun loadRecentDocuments() {
        appPreferences.recentDocuments.onEach { documents ->
            _state.value = _state.value.copy(recentDocuments = documents)
            documentExpiryJob?.cancel()
            documents.minOfOrNull { it.openedAt + RecentDocument.MAX_AGE_MILLIS }
                ?.let { expiresAt ->
                    documentExpiryJob = viewModelScope.launch {
                        delay((expiresAt - System.currentTimeMillis()).coerceAtLeast(1L))
                        appPreferences.pruneExpiredRecentDocuments()
                    }
                }
        }.launchIn(viewModelScope)
    }

    private fun loadStudents() {
        viewModelScope.launch {
            getAllDetailsUseCase().collect { students ->
                _state.value = _state.value.copy(students = students)
            }
        }
    }

    private fun loadLatestUpdates() {
        getLatestNotificationsUseCase(page = 1, category = Constants.ALL_UPDATES)
            .onEach { result ->
                _state.value = when (result) {
                    is Resource.Success ->
                        _state.value.copy(
                            latestUpdates = recentNotifications(result.data.orEmpty()).take(4),
                            updatesLoading = false
                        )

                    is Resource.Error -> _state.value.copy(updatesLoading = false)
                    is Resource.Loading -> _state.value.copy(updatesLoading = true)
                }
            }
            .launchIn(viewModelScope)
    }

    fun deleteAllStudents() {
        viewModelScope.launch { deleteAllStudentDetailsUseCase() }
    }

    fun clearRecentDocuments() {
        viewModelScope.launch { appPreferences.clearRecentDocuments() }
    }

    fun reopenDocument(document: RecentDocument) {
        viewModelScope.launch { appPreferences.recordRecentDocument(document) }
    }
}

internal fun recentNotifications(
    updates: List<com.dhethi.jntuhconnect.domain.model.LatestNotification>,
    now: Calendar = Calendar.getInstance()
): List<com.dhethi.jntuhconnect.domain.model.LatestNotification> {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val today = formatter.format(now.time)
    val yesterdayCalendar = now.clone() as Calendar
    yesterdayCalendar.add(Calendar.DAY_OF_YEAR, -1)
    val yesterday = formatter.format(yesterdayCalendar.time)
    return updates.filter { it.releaseDate.take(10) == today || it.releaseDate.take(10) == yesterday }
}
