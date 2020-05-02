package de.helfenkannjeder.helfomat.core.organization

import java.time.DayOfWeek
import java.time.LocalTime

/**
 * @author Valentin Zickner
 */
data class AttendanceTime(
    val day: DayOfWeek,
    val start: LocalTime,
    val end: LocalTime,
    val note: String? = null,
    val groups: List<Group> = emptyList()
)