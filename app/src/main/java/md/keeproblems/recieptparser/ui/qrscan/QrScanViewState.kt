package md.keeproblems.recieptparser.ui.qrscan


import md.keeproblems.recieptparser.domain.models.Product

internal data class QrScanViewState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String = ""
) {
    companion object {
        val empty = QrScanViewState()
    }
}