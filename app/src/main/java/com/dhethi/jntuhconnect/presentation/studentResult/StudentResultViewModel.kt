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
import com.dhethi.jntuhconnect.domain.use_case.get_credits.GetCreditsUseCase
import com.dhethi.jntuhconnect.domain.use_case.save_student_details.SaveStudentDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentResultViewModel @Inject constructor(
    private val getAcademicResultUseCase: GetAcademicResultUseCase,
    private val getAllResultsUseCase: GetAllResultsUseCase,
    private val getBacklogResultUseCase: GetBacklogResultUseCase,
    private val getCreditsUseCase: GetCreditsUseCase,
    private val saveStudentDetailsUseCase: SaveStudentDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(StudentResultState())
    val state: State<StudentResultState> = _state

    init {
        val rollNumber = savedStateHandle.get<String>(Constants.PARAM_ROLL_NUMBER)
        val startTab = savedStateHandle.get<String>("startTab").orEmpty()
        if (!rollNumber.isNullOrBlank()) {
            val tab = if (startTab in Constants.STUDENT_RESULT_TABS) startTab
            else Constants.ACADEMIC_RESULTS
            _state.value = _state.value.copy(rollNumber = rollNumber, currentTab = tab)
            fetchAcademicResult(rollNumber)
            // Kick off the deep-linked tab's fetch too (hero always needs academic).
            if (tab != Constants.ACADEMIC_RESULTS) ensureTabData(tab)
        }
    }

    fun setSelectedTab(tab: String) {
        _state.value = _state.value.copy(currentTab = tab)
        ensureTabData(tab)
    }

    private fun ensureTabData(tab: String) {
        val roll = _state.value.rollNumber
        when (tab) {
            Constants.ALL_RESULTS ->
                if (_state.value.allResult.isNullOrEmpty()) fetchAllResults(roll)

            Constants.BACKLOG_RESULTS ->
                if (_state.value.backlogResult == null) fetchBacklogResults(roll)

            Constants.CREDIT_RESULTS ->
                if (_state.value.creditsResult == null) fetchCredits(roll)
        }
    }

    private fun fetchAcademicResult(rollNumber: String) {
        getAcademicResultUseCase(rollNumber).onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = _state.value.copy(isLoading = true, error = "")
                is Resource.Error -> _state.value = _state.value.copy(
                    isLoading = false,
                    error = result.message ?: "An unexpected error occurred!!"
                )

                is Resource.Success -> {
                    val data = result.data
                    _state.value = _state.value.copy(
                        academicResult = data?.academicResult,
                        studentDetails = data?.details,
                        isLoading = false,
                        error = ""
                    )
                    viewModelScope.launch { saveStudentDetails(data?.details, data?.academicResult) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchAllResults(rollNumber: String) {
        getAllResultsUseCase(rollNumber).onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = _state.value.copy(allLoading = true, allError = "")
                is Resource.Error -> _state.value =
                    _state.value.copy(allLoading = false, allError = result.message ?: "Failed to load")

                is Resource.Success -> _state.value = _state.value.copy(
                    allResult = result.data?.results ?: emptyList(),
                    studentDetails = _state.value.studentDetails ?: result.data?.details,
                    allLoading = false,
                    allError = ""
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchBacklogResults(rollNumber: String) {
        getBacklogResultUseCase(rollNumber).onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = _state.value.copy(backlogLoading = true, backlogError = "")
                is Resource.Error -> _state.value =
                    _state.value.copy(backlogLoading = false, backlogError = result.message ?: "Failed to load")

                is Resource.Success -> _state.value = _state.value.copy(
                    backlogResult = result.data?.backlogResult,
                    studentDetails = _state.value.studentDetails ?: result.data?.details,
                    backlogLoading = false,
                    backlogError = ""
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchCredits(rollNumber: String) {
        getCreditsUseCase(rollNumber).onEach { result ->
            when (result) {
                is Resource.Loading -> _state.value = _state.value.copy(creditsLoading = true, creditsError = "")
                is Resource.Error -> _state.value =
                    _state.value.copy(creditsLoading = false, creditsError = result.message ?: "Failed to load")

                is Resource.Success -> _state.value = _state.value.copy(
                    creditsResult = result.data,
                    creditsLoading = false,
                    creditsError = ""
                )
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun saveStudentDetails(details: Details?, results: AcademicResult?) {
        if (details == null || results == null) return
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

    fun reloadStudentResults() {
        fetchAcademicResult(rollNumber = _state.value.rollNumber)
    }
}
