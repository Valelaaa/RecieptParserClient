package md.keeproblems.recieptparser.domain.repository

import md.keeproblems.recieptparser.domain.models.Product
import md.keeproblems.recieptparser.domain.models.TotalPrice

internal interface ReceiptRepository {
    suspend fun getProducts(url: String): Result<List<Product>>
    suspend fun getTotalPrice(url: String): Result<TotalPrice>
}
