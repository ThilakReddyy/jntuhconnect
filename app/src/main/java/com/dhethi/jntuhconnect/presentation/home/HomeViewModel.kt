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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllDetailsUseCase: GetAllStudentDetailsUseCase,
    private val deleteAllStudentDetailsUseCase: DeleteAllStudentDetailsUseCase,
    private val getLatestNotificationsUseCase: GetLatestNotificationsUseCase
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {
        loadStudents()
        loadLatestUpdates()
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
                            latestUpdates = (result.data ?: emptyList()).take(4),
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
}
