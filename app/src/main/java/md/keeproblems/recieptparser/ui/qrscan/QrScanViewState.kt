package md.keeproblems.recieptparser.ui.qrscan


import md.keeproblems.recieptparser.domain.models.Product
import md.keeproblems.recieptparser.domain.models.PriceInfo

internal data class QrScanViewState(
    val products: List<Product> = emptyList(),
    val companyName:String = "",
    val priceInfo: PriceInfo = PriceInfo("0"),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
) {
    companion object {
        val empty = QrScanViewState()
    }
}