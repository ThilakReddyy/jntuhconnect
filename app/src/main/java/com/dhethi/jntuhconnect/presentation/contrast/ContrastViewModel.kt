package com.dhethi.jntuhconnect.presentation.contrast

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.domain.model.ResultContrast
import com.dhethi.jntuhconnect.domain.use_case.get_contrast.GetResultContrastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class ContrastState(
    val roll1: String = "",
    val roll2: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val contrast: ResultContrast? = null
)

@HiltViewModel
class ContrastViewModel @Inject constructor(
    private val getResultContrastUseCase: GetResultContrastUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ContrastState())
    val state: State<ContrastState> = _state

    fun updateRoll1(value: String) {
        _state.value = _state.value.copy(roll1 = value.uppercase().take(10))
    }

    fun updateRoll2(value: String) {
        _state.value = _state.value.copy(roll2 = value.uppercase().take(10))
    }

    fun compare() {
        val r1 = _state.value.roll1
        val r2 = _state.value.roll2
        if (r1.length != 10 || r2.length != 10) {
            _state.value = _state.value.copy(error = "Enter two valid 10-character roll numbers.")
            return
        }
        if (r1 == r2) {
            _state.value = _state.value.copy(error = "Please enter two different roll numbers.")
            return
        }
        getResultContrastUseCase(r1, r2).onEach { result ->
            _state.value = when (result) {
                is Resource.Loading -> _state.value.copy(isLoading = true, error = "", contrast = null)
                is Resource.Error -> _state.value.copy(isLoading = false, error = result.message ?: "Failed")
                is Resource.Success -> _state.value.copy(isLoading = false, error = "", contrast = result.data)
            }
        }.launchIn(viewModelScope)
    }
}
