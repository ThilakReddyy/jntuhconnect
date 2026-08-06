package com.dhethi.jntuhconnect.presentation.careers

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.common.friendlyMessage
import com.dhethi.jntuhconnect.data.remote.JntuhConnectApi
import com.dhethi.jntuhconnect.data.remote.dto.JobDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

data class CareersState(
    val jobs: List<JobDto> = emptyList(),
    val page: Int = 1,
    val hasMore: Boolean = false,
    val keyword: String = "",
    val type: String = "",
    val companyType: String = "",
    val remote: Boolean? = null,
    val isLoading: Boolean = true,
    val isLoadingMore: Boolean = false,
    val error: String = ""
) {
    val activeFilterCount: Int
        get() = listOf(type.isNotBlank(), companyType.isNotBlank(), remote != null).count { it }
}

@HiltViewModel
class CareersViewModel @Inject constructor(
    private val api: JntuhConnectApi
) : ViewModel() {
    private val _state = mutableStateOf(CareersState())
    val state: State<CareersState> = _state
    private var requestJob: Job? = null

    init {
        load(page = 1, append = false)
    }

    fun search(keyword: String) {
        val cleaned = keyword.trim()
        if (cleaned == _state.value.keyword) return
        _state.value = _state.value.copy(keyword = cleaned)
        reload()
    }

    fun applyFilters(type: String, companyType: String, remote: Boolean?) {
        if (
            type == _state.value.type &&
            companyType == _state.value.companyType &&
            remote == _state.value.remote
        ) return
        _state.value = _state.value.copy(
            type = type,
            companyType = companyType,
            remote = remote
        )
        reload()
    }

    fun clearFilters() {
        _state.value = _state.value.copy(type = "", companyType = "", remote = null)
        reload()
    }

    fun refresh() = reload()

    fun retry() {
        if (_state.value.jobs.isEmpty()) reload() else loadNextPage()
    }

    fun loadNextPage() {
        val current = _state.value
        if (current.isLoading || current.isLoadingMore || !current.hasMore) return
        load(page = current.page + 1, append = true)
    }

    private fun reload() {
        requestJob?.cancel()
        _state.value = _state.value.copy(
            jobs = emptyList(), page = 1, hasMore = false,
            isLoading = true, isLoadingMore = false, error = ""
        )
        load(page = 1, append = false)
    }

    private fun load(page: Int, append: Boolean) {
        if (append && (_state.value.isLoadingMore || !_state.value.hasMore)) return
        requestJob?.cancel()
        _state.value = _state.value.copy(
            isLoading = !append,
            isLoadingMore = append,
            error = ""
        )
        val filters = _state.value
        requestJob = viewModelScope.launch {
            try {
                val response = api.getJobs(
                    page = page,
                    keyword = filters.keyword,
                    type = filters.type,
                    companyType = filters.companyType,
                    remote = filters.remote
                )
                _state.value = _state.value.copy(
                    jobs = mergeJobs(_state.value.jobs, response.jobs, append),
                    page = page,
                    hasMore = response.hasMore,
                    isLoading = false,
                    isLoadingMore = false,
                    error = ""
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    error = when (error) {
                        is HttpException -> error.friendlyMessage()
                        is IOException -> "Check your internet connection and try again."
                        else -> "Couldn't load opportunities. Please try again."
                    }
                )
            }
        }
    }
}

internal fun mergeJobs(
    current: List<JobDto>,
    incoming: List<JobDto>,
    append: Boolean
): List<JobDto> = (if (append) current + incoming else incoming).distinctBy { it.id }
