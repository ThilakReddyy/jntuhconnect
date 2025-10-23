package com.dhethi.jntuhconnect.presentation.studentResult

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.common.Resource
import com.dhethi.jntuhconnect.data.local.entities.StudentDetailsEntity
import com.dhethi.jntuhconnect.domain.model.AcademicResult
import com.dhethi.jntuhconnect.domain.model.Details
import com.dhethi.jntuhconnect.domain.use_case.get_academic_result.GetAcademicResultUseCase
import com.dhethi.jntuhconnect.domain.use_case.get_all_results.GetAllResultsUseCase
import com.dhethi.jntuhconnect.domain.use_case.get_backlog_results.GetBacklogResultUseCase
import com.dhethi.jntuhconnect.domain.use_case.save_student_details.SaveStudentDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentResultViewModel @Inject constructor(
    private val getAcademicResultUseCase: GetAcademicResultUseCase,
    private val getAllResultsUseCase: GetAllResultsUseCase,
    private val getBacklogResultUseCase: GetBacklogResultUseCase,
    private val saveStudentDetailsUseCase: SaveStudentDetailsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(StudentResultState())
    val state: State<StudentResultState> = _state


    init {
        savedStateHandle.get<String>(Constants.PARAM_ROLL_NUMBER)?.let { rollNumber ->
            _state.value = _state.value.copy(rollNumber = rollNumber)
            fetchAcademicResult(rollNumber)
        }
    }

    // Public function to handle tab selection
    fun setSelectedTab(tab: String) {
        val rollNumber = state.value.rollNumber
        _state.value = _state.value.copy(currentTab = tab)

        when (tab) {
            Constants.ALL_RESULTS -> if (state.value.allResult?.isEmpty() != false) fetchAllResults(
                rollNumber
            )

            Constants.BACKLOG_RESULTS -> if (state.value.backlogResult == null) fetchBacklogResults(
                rollNumber
            )
        }
    }

    private  fun fetchAcademicResult(rollNumber: String) {
        collectResult(
            flow = getAcademicResultUseCase(rollNumber),
            onSuccess = { data ->
                _state.value = _state.value.copy(
                    academicResult = data?.academicResult,
                    studentDetails = data?.details,
                    isLoading = false,
                    error = ""
                )

                viewModelScope.launch {
                    saveStudentDetails(data?.details, data?.academicResult)
                }


            },
            rollNumber = rollNumber
        )
    }

    private fun fetchAllResults(rollNumber: String) {
        collectResult(
            flow = getAllResultsUseCase(rollNumber),
            onSuccess = { data ->
                _state.value = _state.value.copy(
                    allResult = data?.results ?: emptyList(),
                    studentDetails = data?.details,
                    isLoading = false,
                    error = ""
                )
            },
            rollNumber = rollNumber
        )
    }

    private fun fetchBacklogResults(rollNumber: String) {
        collectResult(
            flow = getBacklogResultUseCase(rollNumber),
            onSuccess = { data ->
                _state.value = _state.value.copy(
                    backlogResult = data?.backlogResult,
                    studentDetails = data?.details,
                    isLoading = false,
                    error = ""
                )
            },
            rollNumber = rollNumber
        )
    }

    // --------------------------
    // Generic collector for all results
    // --------------------------
    private fun <T> collectResult(
        flow: Flow<Resource<T>>,
        onSuccess: (T?) -> Unit,
        rollNumber: String
    ) {
        flow.onEach { result ->
            when (result) {
                is Resource.Success -> onSuccess(result.data)

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        error = result.message ?: "An unexpected error occurred!!",
                        isLoading = false,
                        rollNumber = rollNumber
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true,
                        error = "",
                        rollNumber = rollNumber
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun saveStudentDetails(details: Details?, results: AcademicResult?) {
        if (details == null || results == null) {
            return;
        }
        saveStudentDetailsUseCase(
            details = StudentDetailsEntity(
                name = details.name,
                rollNumber = details.rollNumber,
                branch = details.branch,
                collegeCode = details.collegeCode,
                semester = results.semesters.size,
                cgpa = results.cgpa,
                backlogs = results.backlogs,
                lastUpdated = System.currentTimeMillis()
            )
        )

    }

      fun reloadStudentResults(){
        fetchAcademicResult(rollNumber=state.value.rollNumber)
    }

}
