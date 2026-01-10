package md.keeproblems.recieptparser.domain.models

internal data class PriceInfo(
    val value: String,
    val currency: Currency = Currency.MDL, // Default in Moldova, so we'll use it
) {
    override fun toString(): String {
        val space = if (currency.noSpace) "" else " "
        return if (currency.prefix) {
            "${currency.symbol}$value"
        } else {
            "$value$space${currency.symbol}"
        }
    }
}


internal enum class Currency(
    val symbol: String,
    val prefix: Boolean,
    val noSpace: Boolean
) {
    MDL("MDL", prefix = false, noSpace = false),
    USD("$",  prefix = true,  noSpace = true),  // $100
    EUR("€",  prefix = true,  noSpace = true),  // €100
    USDT("USDT", prefix = false, noSpace = false),
    RUB("₽", prefix = false, noSpace = true)    // 100₽
}