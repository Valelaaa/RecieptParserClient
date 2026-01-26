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
import md.keeproblems.recieptparser.data.entities.ProductDescription
import md.keeproblems.recieptparser.data.entities.ProductPayloadDto
import md.keeproblems.recieptparser.data.repository.NormalizedProductsRepository
import md.keeproblems.recieptparser.data.repository.UserReceiptRepository
import md.keeproblems.recieptparser.domain.models.Category
import md.keeproblems.recieptparser.domain.usecases.GetProductsUseCase
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
internal class QrScanViewModel @Inject constructor(
    val getProductsUseCase: GetProductsUseCase,
    val userReceiptRepository: UserReceiptRepository,
    val normalizedProductsRepository: NormalizedProductsRepository,
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

    fun updateScannedQr(qr: String?) {
        _state.update { it.copy(scannedQr = qr) }
    }

    fun updateProducts(url: String) {
        viewModelScope.launch(coroutineExceptionHandler + IO) {
            _state.update { it.copy(isLoading = true) }
            println("!!! beforeUSeCase:")
            var products = getProductsUseCase(url)
            val normalizedProducts = normalizedProductsRepository.normalizeProducts(
                ProductPayloadDto(
                    products.products.map {
                        ProductDescription(
                            id = it.productName.hashCode().toString(), name = it.productName
                        )
                    }
                )
            ).getOrThrow()

            println("!!! normalizedProducts: ${normalizedProducts}")
            val normalizedMap =
                normalizedProducts?.normalizedProducts?.associateBy { it.originalName }
            products = products.copy(
                products = products.products.map { original ->
                    normalizedMap?.get(original.productName)?.let { normalized ->
                        original.copy(
                            productName = normalized.normalizedName,
                            productDescription = normalized.flavor + " " + normalized.size,
                            category = Category(
                                name = normalized.category,
                                colorValue = normalized.categoryColor
                            ),
                        )
                    } ?: original
                }
            )

            _state.update {
                it.copy(
                    companyName = products.companyName,
                    products = products.products,
                    totalAmount = products.priceInfo,
                    isLoading = false,
                    receiptData = products
                )
            }
        }
    }

    fun onSaveReceipt() {
        viewModelScope.launch {
            userReceiptRepository.saveProduct(state.value.receiptData)
        }
    }


    fun clearError() {
        _state.update { it.copy(errorMessage = "") }
    }

}