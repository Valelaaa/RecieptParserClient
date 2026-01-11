package md.keeproblems.recieptparser.ui.qrscan


import md.keeproblems.recieptparser.domain.models.Product
import md.keeproblems.recieptparser.domain.models.PriceInfo
import md.keeproblems.recieptparser.domain.models.ReceiptData

internal data class QrScanViewState(
    val receiptData: ReceiptData? = null,
    val products: List<Product> = emptyList(),
    val companyName: String = "",
    val totalAmount: PriceInfo = PriceInfo("0"),
    val isLoading: Boolean = false,
    val scannedQr:String? = null,
    val errorMessage: String = ""
) {
    companion object {
        val empty = QrScanViewState()
    }
}