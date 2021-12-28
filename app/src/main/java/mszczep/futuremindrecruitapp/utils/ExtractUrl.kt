package mszczep.futuremindrecruitapp.utils

import android.util.Patterns

fun String.extractUrl(): String? {
    val matcher = Patterns.WEB_URL.matcher(this)
    return if (matcher.find()) {
        matcher.group()
    } else
        null
}