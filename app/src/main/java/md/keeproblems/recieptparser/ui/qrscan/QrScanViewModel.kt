package md.keeproblems.recieptparser.ui.qrscan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.keeproblems.recieptparser.domain.usecases.GetProductsUseCase
import javax.inject.Inject

@HiltViewModel
internal class QrScanViewModel @Inject constructor(
    val getProductsUseCase: GetProductsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(QrScanViewState.empty)
    val state = _state.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = QrScanViewState.empty
    )

    val coroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            _state.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Something went wrong or wrong qr code"
                )
            }
        }
    }

    fun updateProducts(url: String) {
        viewModelScope.launch(coroutineExceptionHandler + IO) {
            _state.update { it.copy(isLoading = true) }
            println("!!! beforeUSeCase:")
            val products = getProductsUseCase(url)
            _state.update {
                it.copy(
                    companyName = products.companyName,
                    products = products.products,
                    priceInfo = products.priceInfo,
                    isLoading = false
                )
            }
        }
    }

    fun onSaveReceipt() {

    }

    fun onShareReceipt() {

    }

    fun clearError() {
        _state.update { it.copy(errorMessage = "") }
    }
}