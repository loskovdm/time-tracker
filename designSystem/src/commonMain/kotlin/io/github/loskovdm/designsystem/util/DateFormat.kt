package io.github.loskovdm.designsystem.util

import androidx.compose.runtime.Composable
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.month_april_short
import timetracker.designsystem.generated.resources.month_august_short
import timetracker.designsystem.generated.resources.month_december_short
import timetracker.designsystem.generated.resources.month_february_short
import timetracker.designsystem.generated.resources.month_january_short
import timetracker.designsystem.generated.resources.month_july_short
import timetracker.designsystem.generated.resources.month_june_short
import timetracker.designsystem.generated.resources.month_march_short
import timetracker.designsystem.generated.resources.month_may_short
import timetracker.designsystem.generated.resources.month_november_short
import timetracker.designsystem.generated.resources.month_october_short
import timetracker.designsystem.generated.resources.month_september_short
import timetracker.designsystem.generated.resources.weekday_friday_short
import timetracker.designsystem.generated.resources.weekday_monday_short
import timetracker.designsystem.generated.resources.weekday_saturday_short
import timetracker.designsystem.generated.resources.weekday_sunday_short
import timetracker.designsystem.generated.resources.weekday_thursday_short
import timetracker.designsystem.generated.resources.weekday_tuesday_short
import timetracker.designsystem.generated.resources.weekday_wednesday_short
import kotlin.time.Clock

@Composable
fun formatDateToString(date: LocalDate): String {
    val weekday = when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> stringResource(Res.string.weekday_monday_short)
        DayOfWeek.TUESDAY -> stringResource(Res.string.weekday_tuesday_short)
        DayOfWeek.WEDNESDAY -> stringResource(Res.string.weekday_wednesday_short)
        DayOfWeek.THURSDAY -> stringResource(Res.string.weekday_thursday_short)
        DayOfWeek.FRIDAY -> stringResource(Res.string.weekday_friday_short)
        DayOfWeek.SATURDAY -> stringResource(Res.string.weekday_saturday_short)
        DayOfWeek.SUNDAY -> stringResource(Res.string.weekday_sunday_short)
    }
    val month = when (date.month) {
        Month.JANUARY -> stringResource(Res.string.month_january_short)
        Month.FEBRUARY -> stringResource(Res.string.month_february_short)
        Month.MARCH -> stringResource(Res.string.month_march_short)
        Month.APRIL -> stringResource(Res.string.month_april_short)
        Month.MAY -> stringResource(Res.string.month_may_short)
        Month.JUNE -> stringResource(Res.string.month_june_short)
        Month.JULY -> stringResource(Res.string.month_july_short)
        Month.AUGUST -> stringResource(Res.string.month_august_short)
        Month.SEPTEMBER -> stringResource(Res.string.month_september_short)
        Month.OCTOBER -> stringResource(Res.string.month_october_short)
        Month.NOVEMBER -> stringResource(Res.string.month_november_short)
        Month.DECEMBER -> stringResource(Res.string.month_december_short)
    }
    val day = date.day.toString().padStart(2, '0')
    val currentYear = Clock.System.now().
        toLocalDateTime(TimeZone.currentSystemDefault())
        .year
    val yearPart = if (date.year != currentYear) {
        " ${date.year}"
    } else {
        ""
    }

    return "$weekday, $day $month$yearPart"
}