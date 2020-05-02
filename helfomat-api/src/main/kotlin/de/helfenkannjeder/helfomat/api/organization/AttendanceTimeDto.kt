package de.helfenkannjeder.helfomat.api.organization

import java.time.DayOfWeek
import java.time.LocalTime

/**
 * @author Valentin Zickner
 */
data class AttendanceTimeDto(
    val day: DayOfWeek,
    val start: LocalTime,
    val end: LocalTime,
    val note: String?,
    val groups: List<GroupDto>
)