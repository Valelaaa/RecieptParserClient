package md.keeproblems.recieptparser.ui.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.keeproblems.recieptparser.data.repository.UserReceiptRepository
import md.keeproblems.recieptparser.domain.models.PriceInfo
import md.keeproblems.recieptparser.domain.models.ReceiptData
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    private val receiptRepository: UserReceiptRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardViewState.EMPTY)
    val state: StateFlow<DashboardViewState> = _state

    init {
        refresh()
    }

    fun refresh() {
        refreshHistory()
        refreshSpentAmounts()
    }

    fun refreshHistory() {
        viewModelScope.launch {
            val history = receiptRepository.getFirstNProducts(3)
            _state.update { it.copy(history = history) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshSpentAmounts() {
        viewModelScope.launch {
            val allReceipts = receiptRepository.getAllProducts()

            val spentWeek = calculateSpentThisWeek(allReceipts)
            val spentMonth = calculateSpentThisMonth(allReceipts)
            val spentTotal = calculateSpentTotal(allReceipts)
            _state.update {
                it.copy(
                    spentWeek = spentWeek,
                    spentMonth = spentMonth,
                    spentTotal = spentTotal
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateSpentThisWeek(receipts: List<ReceiptData>): PriceInfo {
        val today = LocalDate.now()
        val startOfWeek = today.with(DayOfWeek.MONDAY)
        val endOfWeek = today.with(DayOfWeek.SUNDAY)

        val sum = receipts
            .filter { it.receiptDate != null && it.receiptDate in startOfWeek..endOfWeek }
            .sumOf { it.priceInfo.value.toDoubleOrNull() ?: 0.0 }

        return PriceInfo("%.2f".format(sum))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateSpentThisMonth(receipts: List<ReceiptData>): PriceInfo {
        val today = LocalDate.now()
        val startOfMonth = today.withDayOfMonth(1)
        val endOfMonth = today.withDayOfMonth(today.lengthOfMonth())

        val sum = receipts
            .filter { it.receiptDate != null && it.receiptDate in startOfMonth..endOfMonth }
            .sumOf { it.priceInfo.value.toDoubleOrNull() ?: 0.0 }

        return PriceInfo("%.2f".format(sum))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateSpentTotal(receipts: List<ReceiptData>): PriceInfo {
        val sum = receipts
            .sumOf { it.priceInfo.value.toDoubleOrNull() ?: 0.0 }

        return PriceInfo("%.2f".format(sum))
    }
}