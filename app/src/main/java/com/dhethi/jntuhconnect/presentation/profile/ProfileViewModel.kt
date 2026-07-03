package com.dhethi.jntuhconnect.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import com.dhethi.jntuhconnect.domain.use_case.get_all_student_details.GetAllStudentDetailsUseCase
import com.dhethi.jntuhconnect.presentation.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    getAllDetailsUseCase: GetAllStudentDetailsUseCase
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = appPreferences.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    val notificationsEnabled: StateFlow<Boolean> = appPreferences.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    private val _students = mutableStateOf<List<StudentDetailsEntity>>(emptyList())
    val students: State<List<StudentDetailsEntity>> = _students

    init {
        viewModelScope.launch {
            getAllDetailsUseCase().collect { _students.value = it }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { appPreferences.setThemeMode(mode) }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { appPreferences.setNotificationsEnabled(enabled) }
    }
}
