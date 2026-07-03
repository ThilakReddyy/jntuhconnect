package com.dhethi.jntuhconnect.presentation.classresult

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.domain.model.ClassBacklogStudent
import com.dhethi.jntuhconnect.domain.model.ClassStudent
import com.dhethi.jntuhconnect.domain.use_case.get_class_result.GetClassResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

const val CLASS_TAB_ACADEMIC = "Academic"
const val CLASS_TAB_BACKLOGS = "Backlogs"

data class ClassResultState(
    val roll: String = "",
    val type: String = CLASS_TAB_ACADEMIC,
    val isLoading: Boolean = false,
    val error: String = "",
    val academic: List<ClassStudent> = emptyList(),
    val backlogs: List<ClassBacklogStudent> = emptyList(),
    val loaded: Boolean = false
)

@HiltViewModel
class ClassResultViewModel @Inject constructor(
    private val getClassResultUseCase: GetClassResultUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ClassResultState())
    val state: State<ClassResultState> = _state

    fun updateRoll(value: String) {
        _state.value = _state.value.copy(roll = value.uppercase().take(10))
    }

    fun setType(type: String) {
        if (type == _state.value.type) return
        _state.value = _state.value.copy(type = type)
        if (_state.value.loaded && _state.value.roll.length == 10) load()
    }

    fun load() {
        val roll = _state.value.roll
        if (roll.length != 10) {
            _state.value = _state.value.copy(error = "Enter a valid 10-character roll number.")
            return
        }
        if (_state.value.type == CLASS_TAB_ACADEMIC) loadAcademic(roll) else loadBacklogs(roll)
    }

    private fun loadAcademic(roll: String) {
        getClassResultUseCase.academic(roll).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isLoading = true, error = "")
                is Resource.Error -> _state.value.copy(isLoading = false, error = result.message ?: "Failed", loaded = true)
                is Resource.Success -> _state.value.copy(
                    isLoading = false,
                    error = "",
                    academic = result.data ?: emptyList(),
                    loaded = true
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun loadBacklogs(roll: String) {
        getClassResultUseCase.backlogs(roll).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isLoading = true, error = "")
                is Resource.Error -> _state.value.copy(isLoading = false, error = result.message ?: "Failed", loaded = true)
                is Resource.Success -> _state.value.copy(
                    isLoading = false,
                    error = "",
                    backlogs = result.data ?: emptyList(),
                    loaded = true
                )
            }
        }.launchIn(viewModelScope)
    }
}
