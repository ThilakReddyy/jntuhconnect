package com.dhethi.jntuhconnect.presentation.careers

import com.dhethi.jntuhconnect.data.remote.dto.JobDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CareersViewModelTest {
    @Test
    fun `pagination merge keeps one job per backend id`() {
        val current = listOf(JobDto(id = "job-1", title = "Software Engineer Intern"))
        val incoming = listOf(
            JobDto(id = "job-1", title = "Software Engineer Intern"),
            JobDto(id = "job-2", title = "Graduate Engineer")
        )

        val merged = mergeJobs(current, incoming, append = true)

        assertEquals(listOf("job-1", "job-2"), merged.map { it.id })
    }

    @Test
    fun `fresh search replaces prior results`() {
        val current = listOf(JobDto(id = "old"))
        val incoming = listOf(JobDto(id = "new"))

        assertEquals(listOf("new"), mergeJobs(current, incoming, append = false).map { it.id })
    }
}
