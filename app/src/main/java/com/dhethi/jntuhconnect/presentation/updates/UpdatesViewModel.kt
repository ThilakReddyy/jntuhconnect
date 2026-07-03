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
import javax.inject.Inject

@HiltViewModel
class UpdatesViewModel @Inject constructor(
    private val getLatestNotificationsUseCase: GetLatestNotificationsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(UpdatesState())
    val state: State<UpdatesState> = _state
    private var isLoadingNextPage = false

    init {
        loadNotifications()
    }

    private fun loadNotifications(isNextPage: Boolean = false) {
        if (isLoadingNextPage || (isNextPage && _state.value.endReached)) return
        isLoadingNextPage = true
        _state.value = _state.value.copy(isLoading = true, error = "")

        val s = _state.value
        getLatestNotificationsUseCase(
            page = s.page,
            category = s.currentTab,
            regulation = s.regulation,
            degree = s.degreeCode,
            year = s.year,
            title = s.titleQuery
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val newItems = result.data ?: emptyList()
                    val updatedList = if (isNextPage) _state.value.updates + newItems else newItems
                    _state.value = _state.value.copy(
                        updates = updatedList,
                        isLoading = false,
                        endReached = newItems.isEmpty()
                    )
                }

                is Resource.Error ->
                    _state.value = _state.value.copy(isLoading = false, error = result.message ?: "An unexpected error occurred")

                is Resource.Loading -> _state.value = _state.value.copy(isLoading = true)
            }
            isLoadingNextPage = false
        }.launchIn(viewModelScope)
    }

    private fun reset() {
        _state.value = _state.value.copy(page = 1, updates = emptyList(), endReached = false, isLoading = true, error = "")
        loadNotifications(isNextPage = false)
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

    fun retry() = reset()

    fun loadNextPage() {
        if (_state.value.isLoading || _state.value.endReached) return
        _state.value = _state.value.copy(page = _state.value.page + 1)
        loadNotifications(isNextPage = true)
    }
}
