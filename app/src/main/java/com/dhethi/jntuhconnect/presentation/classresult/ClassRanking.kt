package com.dhethi.jntuhconnect.presentation.classresult

import com.dhethi.jntuhconnect.domain.model.ClassStudent
import java.math.BigDecimal

internal data class RankedClassStudent(
    val student: ClassStudent,
    val cgpa: BigDecimal,
    val rank: Int
)

internal data class ClassRanking(
    val ranked: List<RankedClassStudent>,
    val unranked: List<ClassStudent>
) {
    val toppers: List<RankedClassStudent>
        get() = ranked.takeWhile { it.rank == 1 }

    val topCgpa: BigDecimal?
        get() = ranked.firstOrNull()?.cgpa

    val averageCgpa: Double?
        get() = ranked.takeIf { it.isNotEmpty() }?.map { it.cgpa.toDouble() }?.average()

    val studentCount: Int
        get() = ranked.size + unranked.size
}

internal fun rankClassStudents(students: List<ClassStudent>): ClassRanking {
    val classified = students.map { student ->
        val result = student.result
        val cgpa = result?.cgpa
            ?.trim()
            ?.toBigDecimalOrNull()
            ?.takeIf {
                result.backlogs == 0 && it >= BigDecimal.ZERO && it <= BigDecimal.TEN
            }
        student to cgpa
    }

    val sorted = classified
        .mapNotNull { (student, cgpa) -> cgpa?.let { student to it } }
        .sortedWith(
            compareByDescending<Pair<ClassStudent, BigDecimal>> { it.second }
                .thenBy { it.first.details.rollNumber }
        )

    var previousCgpa: BigDecimal? = null
    var currentRank = 0
    val ranked = sorted.mapIndexed { index, (student, cgpa) ->
        if (previousCgpa == null || previousCgpa?.compareTo(cgpa) != 0) {
            currentRank = index + 1
        }
        previousCgpa = cgpa
        RankedClassStudent(student = student, cgpa = cgpa, rank = currentRank)
    }

    return ClassRanking(
        ranked = ranked,
        unranked = classified.filter { it.second == null }.map { it.first }
    )
}
