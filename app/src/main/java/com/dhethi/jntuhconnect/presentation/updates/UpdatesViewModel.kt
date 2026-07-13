package com.dhethi.jntuhconnect.presentation.updates

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.domain.use_case.get_latest_notifications.GetLatestNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class UpdatesViewModel @Inject constructor(
    private val getLatestNotificationsUseCase: GetLatestNotificationsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(UpdatesState())
    val state: State<UpdatesState> = _state
    private var loadJob: Job? = null

    init {
        loadNotifications(page = 1, append = false)
    }

    private fun loadNotifications(page: Int, append: Boolean) {
        if (append && (_state.value.isLoading || _state.value.endReached)) return
        loadJob?.cancel()
        _state.value = _state.value.copy(isLoading = true, error = "")

        val s = _state.value
        getLatestNotificationsUseCase(
            page = page,
            category = s.currentTab,
            regulation = s.regulation,
            degree = s.degreeCode,
            year = s.year,
            title = s.titleQuery
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val newItems = result.data ?: emptyList()
                    _state.value = _state.value.copy(
                        page = page,
                        updates = mergeUpdates(_state.value.updates, newItems, append),
                        isLoading = false,
                        endReached = newItems.isEmpty()
                    )
                }

                is Resource.Error ->
                    _state.value = _state.value.copy(isLoading = false, error = result.message ?: "An unexpected error occurred")

                is Resource.Loading -> Unit
            }
        }.also { flow ->
            loadJob = flow.launchIn(viewModelScope)
        }
    }

    private fun reset() {
        _state.value = _state.value.copy(page = 1, updates = emptyList(), endReached = false, isLoading = true, error = "")
        loadNotifications(page = 1, append = false)
    }

    fun switchCategory(category: String) {
        if (category == _state.value.currentTab) return
        _state.value = _state.value.copy(currentTab = category)
        reset()
    }

    fun applyFilters(degreeLabel: String, degreeCode: String, regulation: String, year: String, title: String) {
        _state.value = _state.value.copy(
            degreeLabel = degreeLabel,
            degreeCode = degreeCode,
            regulation = regulation,
            year = year,
            titleQuery = title
        )
        reset()
    }

    fun clearFilters() {
        _state.value = _state.value.copy(
            degreeLabel = "", degreeCode = "", regulation = "", year = "", titleQuery = ""
        )
        reset()
    }

    fun retry() {
        if (_state.value.updates.isEmpty()) reset() else loadNextPage()
    }

    fun loadNextPage() {
        if (_state.value.isLoading || _state.value.endReached) return
        loadNotifications(page = _state.value.page + 1, append = true)
    }
}

internal fun mergeUpdates(
    current: List<com.dhethi.jntuhconnect.domain.model.LatestNotification>,
    incoming: List<com.dhethi.jntuhconnect.domain.model.LatestNotification>,
    append: Boolean
): List<com.dhethi.jntuhconnect.domain.model.LatestNotification> =
    (if (append) current + incoming else incoming).distinctBy {
        "${it.link}|${it.title}|${it.releaseDate}"
    }
