package md.keeproblems.recieptparser.domain.usecases

import md.keeproblems.recieptparser.domain.models.Product

internal interface GetProductsUseCase {
    suspend operator fun invoke(url: String): List<Product>
}

