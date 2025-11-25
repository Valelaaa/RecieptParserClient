package md.keeproblems.recieptparser.domain.usecases.impl

import md.keeproblems.recieptparser.data.repository.ReceiptRepositoryImpl
import md.keeproblems.recieptparser.domain.models.Product
import md.keeproblems.recieptparser.domain.repository.ReceiptRepository
import md.keeproblems.recieptparser.domain.usecases.GetProductsUseCase

internal class GetProductsUseCaseImpl(
    private val receiptRepository: ReceiptRepository = ReceiptRepositoryImpl()
) : GetProductsUseCase {
    override suspend fun invoke(url: String): List<Product> {
        return receiptRepository.getProducts(url).getOrThrow()
    }
}