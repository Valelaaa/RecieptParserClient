package md.keeproblems.recieptparser.ui.dashboard

import md.keeproblems.recieptparser.domain.models.PriceInfo
import md.keeproblems.recieptparser.domain.models.ReceiptData

internal data class DashboardViewState(
    val isLoading: Boolean = true,
    val history: List<ReceiptData> = emptyList(),
    val spentWeek: PriceInfo = PriceInfo(""),
    val spentMonth: PriceInfo = PriceInfo(""),
    val spentTotal: PriceInfo = PriceInfo("")
) {
    companion object {
        val EMPTY = DashboardViewState()
    }
}
