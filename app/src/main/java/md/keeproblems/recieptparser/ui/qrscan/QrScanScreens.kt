package md.keeproblems.recieptparser.ui.qrscan

internal sealed class QrScanScreens(val route: String) {
    data object SuccessScreen : QrScanScreens("success_screen")
    data object ScannedList : QrScanScreens("scanned_list")
    data object ReceiptDetailsScreen : QrScanScreens("receipt_details_screen")
}