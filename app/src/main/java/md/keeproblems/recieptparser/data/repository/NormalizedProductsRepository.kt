package md.keeproblems.recieptparser.data.repository

import md.keeproblems.recieptparser.data.apiservices.AiApiService
import md.keeproblems.recieptparser.data.entities.NormalizedReceiptResponse
import md.keeproblems.recieptparser.data.entities.ProductPayloadDto
import javax.inject.Inject


class NormalizedProductsRepository @Inject constructor(private val apiService: AiApiService) {
    suspend fun normalizeProducts(products: ProductPayloadDto): Result<NormalizedReceiptResponse> {
        return try {
            val response = apiService.normalizeProducts(products)
            Result.success(filterCategories(response))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun filterCategories(products: NormalizedReceiptResponse): NormalizedReceiptResponse {
        return products.copy(normalizedProducts = products.normalizedProducts.filterNot {
            it.category.equals(
                "miscellaneous",
                ignoreCase = true
            )
        }
        )
    }
}