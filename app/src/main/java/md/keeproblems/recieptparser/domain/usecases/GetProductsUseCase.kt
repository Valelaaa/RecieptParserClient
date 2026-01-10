package md.keeproblems.recieptparser.domain.usecases

import md.keeproblems.recieptparser.domain.models.Products

internal interface GetProductsUseCase {
    suspend operator fun invoke(url: String): Products
}

