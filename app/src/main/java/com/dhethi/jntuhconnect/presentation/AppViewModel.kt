package com.dhethi.jntuhconnect.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import com.dhethi.jntuhconnect.presentation.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Root-level view model that exposes the persisted theme preference so the whole app can
 * react to light/dark changes made from the Profile screen.
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = appPreferences.themeMode
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { appPreferences.setThemeMode(mode) }
    }
}
