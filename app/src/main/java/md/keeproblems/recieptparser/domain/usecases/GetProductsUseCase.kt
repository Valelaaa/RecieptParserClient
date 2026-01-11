package md.keeproblems.recieptparser.domain.usecases

import md.keeproblems.recieptparser.domain.models.ReceiptData

internal interface GetProductsUseCase {
    suspend operator fun invoke(url: String): ReceiptData
}

