package md.keeproblems.recieptparser.utils.service

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class DateTimeParserService @Inject constructor() {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    fun toTimestampSeconds(date: String, time: String): Long {
        val d = LocalDate.parse(date, dateFormatter)
        val t = LocalTime.parse(time, timeFormatter)
        return LocalDateTime.of(d, t)
            .toEpochSecond(ZoneOffset.UTC)
    }

    fun toTimestampMillis(date: String, time: String): Long {
        val d = LocalDate.parse(date, dateFormatter)
        val t = LocalTime.parse(time, timeFormatter)
        return LocalDateTime.of(d, t)
            .toInstant(ZoneOffset.UTC)
            .toEpochMilli()
    }
}