package com.dhethi.jntuhconnect.presentation.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.data.local.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackPromptViewModel @Inject constructor(
    private val preferences: AppPreferences
) : ViewModel() {
    val state = preferences.feedbackPromptState
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    private val _visible = MutableStateFlow(false)
    val visible = _visible.asStateFlow()

    init {
        viewModelScope.launch { preferences.initializeFeedbackPrompt() }
    }

    suspend fun showIfDue() {
        if (_visible.value) return
        val current = state.value ?: return
        if (current.completed) return
        // Keep unfinished ratings, but reopening the app must respect the cooldown too.
        if (preferences.claimFeedbackPrompt()) {
            _visible.value = true
        }
    }

    fun selectRating(rating: Int) {
        viewModelScope.launch { preferences.setFeedbackRating(rating) }
    }

    fun defer() {
        _visible.value = false
        viewModelScope.launch { preferences.deferFeedbackPrompt() }
    }

    fun complete() {
        _visible.value = false
        viewModelScope.launch { preferences.completeFeedbackPrompt() }
    }
}
