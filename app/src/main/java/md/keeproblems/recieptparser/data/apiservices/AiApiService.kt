package md.keeproblems.recieptparser.data.apiservices

import md.keeproblems.recieptparser.data.entities.NormalizedReceiptResponse
import md.keeproblems.recieptparser.data.entities.ProductPayloadDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AiApiService {
    @POST("api/agent/normalize")
    suspend fun normalizeProducts(@Body products: ProductPayloadDto): NormalizedReceiptResponse
}