package mszczep.futuremindrecruitapp.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun String.formatDate(): String {
    val formatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)

    return LocalDate.parse(this).format(formatter)
}