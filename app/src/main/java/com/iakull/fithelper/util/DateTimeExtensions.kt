package com.iakull.fithelper.util

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.text.SimpleDateFormat
import java.util.*

fun String.toLocalDateTime(): LocalDateTime? = try {
    LocalDateTime.from(ISO_LOCAL_DATE_TIME.parse(this))
} catch (e: Exception) {
    null
}

fun LocalDateTime.toIsoString(): String = format(ISO_LOCAL_DATE_TIME)

private val iso8601DateFormat
    get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

fun String.toDate() = try {
    iso8601DateFormat.parse(this)
} catch (e: Exception) {
    null
}

fun Date.toIsoString(): String = iso8601DateFormat.format(this)

fun Date.toLocalDate(): LocalDate = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDate()

fun Date.atNoon(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal[Calendar.HOUR_OF_DAY] = 12
    cal[Calendar.MINUTE] = 0
    cal[Calendar.SECOND] = 0
    cal[Calendar.MILLISECOND] = 0
    return cal.time
}

fun millisToDays(milliseconds: Long) = (milliseconds / (1000 * 60 * 60 * 24)).toInt()