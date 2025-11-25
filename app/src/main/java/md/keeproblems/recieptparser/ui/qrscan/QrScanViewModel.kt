package md.keeproblems.recieptparser.ui.qrscan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.keeproblems.recieptparser.domain.usecases.GetProductsUseCase
import md.keeproblems.recieptparser.domain.usecases.impl.GetProductsUseCaseImpl

internal class QrScanViewModel(
    val getProductsUseCase: GetProductsUseCase
    = GetProductsUseCaseImpl()
) : ViewModel() {
    private val _state = MutableStateFlow(QrScanViewState.empty)
    val state = _state.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = QrScanViewState.empty
    )

    val coroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, exception ->
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
            val products = getProductsUseCase(url)
            _state.update {
                it.copy(
                    products = products,
                    isLoading = false
                )
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = "") }
    }
}