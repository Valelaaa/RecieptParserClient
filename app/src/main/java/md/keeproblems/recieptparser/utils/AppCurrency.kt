package md.keeproblems.recieptparser.utils

sealed class AppCurrency(
    val title: TextRes,
    val location: CurrencyLocation,
    val symbol: TextRes,
    val code: TextRes
) {
    data object Dollar : AppCurrency(
        title = textResource("Dollar"),
        location = CurrencyLocation.PREFIX,
        symbol = textResource("$"),
        code = textResource("USD")
    )
}

enum class CurrencyLocation {
    PREFIX,
    POSTFIX
}
