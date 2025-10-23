package com.dhethi.jntuhconnect.presentation.updates

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.domain.model.LatestNotification
import com.dhethi.jntuhconnect.domain.use_case.get_latest_notifications.GetLatestNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdatesViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getLatestNotificationsUseCase: GetLatestNotificationsUseCase
) : ViewModel() {
    private val _state = mutableStateOf(UpdatesState())
    val state: State<UpdatesState> = _state
    private var isLoadingNextPage = false

    init {
        loadNotifications()
    }

    private fun loadNotifications(isNextPage: Boolean = false) {
        if (isLoadingNextPage || _state.value.endReached) return
        isLoadingNextPage = true
        _state.value = _state.value.copy(isLoading = true, error = "")

        val page = _state.value.page
        val category = _state.value.currentTab
        getLatestNotificationsUseCase(page,category).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val newItems = result.data ?: emptyList()
                    val updatedList = if (isNextPage) {
                        _state.value.allUpdates + newItems
                    } else {
                        newItems
                    }

                    _state.value = _state.value.copy(
                        allUpdates = updatedList,
                        currentTabUpdates = updatedList,
                        isLoading = false,
                        endReached = newItems.isEmpty()
                    )
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "An unexpected error occurred"
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }

            isLoadingNextPage = false
        }.launchIn(viewModelScope)
    }


    fun switchCurrentTab(currentTab: String) {
        // Reset state for new tab
        _state.value = _state.value.copy(
            currentTab = currentTab,
            page = 1,
            allUpdates = emptyList(),
            currentTabUpdates = emptyList(),
            endReached = false,
            isLoading = true,
            error = ""
        )

        // Fetch notifications for the new tab
        loadNotifications(isNextPage = false)
    }
    fun loadNextPage() {
        if (_state.value.isLoading || _state.value.endReached) return

        _state.value = _state.value.copy(page = _state.value.page + 1)
        loadNotifications(isNextPage = true)
    }

}