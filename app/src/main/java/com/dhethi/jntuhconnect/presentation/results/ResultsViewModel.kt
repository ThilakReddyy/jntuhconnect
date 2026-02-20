package com.dhethi.jntuhconnect.presentation.results

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.domain.use_case.delete_all_student_details.DeleteAllStudentDetailsUseCase
import com.dhethi.jntuhconnect.domain.use_case.get_all_student_details.GetAllStudentDetailsUseCase
import com.dhethi.jntuhconnect.domain.use_case.save_student_details.SaveStudentDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllDetailsUseCase: GetAllStudentDetailsUseCase,
    private val deleteAllStudentDetailsUseCase: DeleteAllStudentDetailsUseCase

) : ViewModel() {

    private val _state = mutableStateOf(ResultsState())
    val state: State<ResultsState> = _state

    init {
        loadAllStudents()
    }

    fun loadAllStudents() {

        viewModelScope.launch {
            getAllDetailsUseCase().collect { students ->
                _state.value = state.value.copy(students = students)
            }

        }

    }

    fun updateRollNumber(newRoll: String) {
        _state.value = _state.value.copy(
            rollNumber = newRoll.uppercase().take(10) // ensures max 10 chars & uppercase
        )
    }

    fun deleteAllStudentDetails(){
        viewModelScope.launch {
            deleteAllStudentDetailsUseCase()
        }

    }

}