package md.keeproblems.recieptparser.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun formatReceiptDate(date: LocalDate?): String {
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when {
        date?.isEqual(today) == true -> "Today"
        date?.isEqual(yesterday) == true -> "Yesterday"
        date?.isAfter(today.minusDays(today.dayOfWeek.ordinal.toLong())) == true -> {
            date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        }

        else -> date?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    } ?: ""
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(date: LocalDate?): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return date?.format(formatter) ?: ""
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(time: LocalTime?): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return time?.format(formatter) ?: ""
}


@RequiresApi(Build.VERSION_CODES.O)
fun parseTime(timeString: String?): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    return LocalTime.parse(timeString, formatter)
}


@RequiresApi(Build.VERSION_CODES.O)
fun parseDate(dateString: String?): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return LocalDate.parse(dateString, formatter)
}
