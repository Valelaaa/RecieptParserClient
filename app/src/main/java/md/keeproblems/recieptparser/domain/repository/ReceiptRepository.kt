package md.keeproblems.recieptparser.domain.repository

import md.keeproblems.recieptparser.domain.models.Products
import md.keeproblems.recieptparser.domain.models.PriceInfo

internal interface ReceiptRepository {
    suspend fun getProducts(url: String): Result<Products>
    suspend fun getTotalPrice(url: String): Result<PriceInfo>
}
