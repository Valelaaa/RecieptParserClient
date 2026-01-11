package md.keeproblems.recieptparser.domain.usecases.impl

import md.keeproblems.recieptparser.domain.models.ReceiptData
import md.keeproblems.recieptparser.domain.repository.ReceiptRepository
import md.keeproblems.recieptparser.domain.usecases.GetProductsUseCase
import javax.inject.Inject

internal class GetProductsUseCaseImpl @Inject constructor(
    private val receiptRepository: ReceiptRepository
) : GetProductsUseCase {
    override suspend fun invoke(url: String): ReceiptData {
        return receiptRepository.getProducts(url).getOrThrow()
    }
}