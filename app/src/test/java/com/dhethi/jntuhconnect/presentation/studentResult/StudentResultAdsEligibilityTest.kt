package com.dhethi.jntuhconnect.presentation.studentResult

import com.dhethi.jntuhconnect.common.Constants
import com.dhethi.jntuhconnect.domain.model.AcademicResult
import com.dhethi.jntuhconnect.domain.model.BacklogResult
import com.dhethi.jntuhconnect.domain.model.CreditsResult
import com.dhethi.jntuhconnect.domain.model.Details
import com.dhethi.jntuhconnect.domain.model.SemesterResult
import com.dhethi.jntuhconnect.domain.model.Semester
import com.dhethi.jntuhconnect.domain.model.StudentCreditsResult
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StudentResultAdsEligibilityTest {
    private val details = Details("00", "", "Test Student", "00TEST0000", "CSE")
    private val academic = StudentResultState(
        studentDetails = details,
        academicLoaded = true,
        academicResult = AcademicResult(
            0, "8.0", 20.0,
            listOf(Semester(0.0, false, "1-1", 20.0, 160.0, "8.0", emptyList()))
        )
    )

    @Test
    fun initialLoadingAndQueuedResultsNeverEnableAds() {
        assertFalse(StudentResultState().selectedResultsReady)
        assertFalse(StudentResultState(isLoading = true).selectedResultsReady)
        assertFalse(StudentResultState(error = "Queued for a fresh check").selectedResultsReady)
    }

    @Test
    fun academicResultsMustBeSuccessfulAndRemainHiddenDuringRetry() {
        assertTrue(academic.selectedResultsReady)
        assertFalse(academic.copy(isLoading = true).selectedResultsReady)
        assertFalse(academic.copy(error = "Offline").selectedResultsReady)
        assertFalse(academic.copy(academicResult = null).selectedResultsReady)
        assertFalse(academic.copy(academicResult = academic.academicResult?.copy(semesters = emptyList())).selectedResultsReady)
        assertFalse(academic.copy(studentDetails = null).selectedResultsReady)
    }

    @Test
    fun loadedHeroDoesNotEnableAdsForLoadingFailedOrEmptyAllResultsTab() {
        val state = academic.copy(currentTab = Constants.ALL_RESULTS)
        assertFalse(state.selectedResultsReady)
        assertFalse(state.copy(allLoading = true).selectedResultsReady)
        assertFalse(state.copy(allLoaded = true, allResult = emptyList()).selectedResultsReady)
        val loaded = state.copy(allLoaded = true, allResult = listOf(SemesterResult("1-1", emptyList())))
        assertTrue(loaded.selectedResultsReady)
        assertFalse(loaded.copy(allError = "Timeout").selectedResultsReady)
        assertFalse(loaded.copy(allLoading = true).selectedResultsReady)
    }

    @Test
    fun zeroBacklogsIsAValidResultButMissingOrFailedBacklogsIsNot() {
        val state = academic.copy(currentTab = Constants.BACKLOG_RESULTS)
        assertFalse(state.copy(backlogLoaded = true).selectedResultsReady)
        val loaded = state.copy(backlogLoaded = true, backlogResult = BacklogResult(emptyList(), 0))
        assertTrue(loaded.selectedResultsReady)
        assertFalse(loaded.copy(backlogError = "Offline").selectedResultsReady)
        assertFalse(loaded.copy(backlogLoading = true).selectedResultsReady)
    }

    @Test
    fun unavailableCreditsDoNotEnableAnAd() {
        val state = academic.copy(currentTab = Constants.CREDIT_RESULTS, creditsLoaded = true)
        assertFalse(state.selectedResultsReady)
        assertFalse(state.copy(
            creditsResult = StudentCreditsResult(details, null, false, "Not applicable")
        ).selectedResultsReady)
        val loaded = state.copy(
            creditsResult = StudentCreditsResult(
                details, CreditsResult(emptyList(), 20.0, 20.0, 160.0), true, null
            )
        )
        assertTrue(loaded.selectedResultsReady)
        assertFalse(loaded.copy(creditsLoading = true).selectedResultsReady)
        assertFalse(loaded.copy(creditsError = "Offline").selectedResultsReady)
    }
}
