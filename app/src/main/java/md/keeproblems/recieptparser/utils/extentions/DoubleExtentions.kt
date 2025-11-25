package md.keeproblems.recieptparser.utils.extentions

import java.text.NumberFormat
import java.util.Locale

fun Double.formatMoney(): String {
    val f = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    return f.format(this)
}