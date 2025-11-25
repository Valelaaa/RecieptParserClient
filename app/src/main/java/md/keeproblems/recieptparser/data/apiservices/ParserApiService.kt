package md.keeproblems.recieptparser.data.apiservices

import md.keeproblems.recieptparser.data.entities.ReceiptResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

internal interface ParserApiService {
    @GET("api/parse")
    suspend fun parseProductsFor(@Query("url", encoded = true) url: String): ReceiptResponseBody

    @POST("api/parse-html")
    suspend fun parseHtml(@Body payload: HtmlPayload): ReceiptResponseBody
}

data class HtmlPayload(val html: String)