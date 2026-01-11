package md.keeproblems.recieptparser.data.repository

import md.keeproblems.recieptparser.domain.models.ReceiptData

internal interface UserReceiptRepository {
    suspend fun getAllProducts(): List<ReceiptData>
    suspend fun getProductsById(id: String): ReceiptData?
    suspend fun getFirstNProducts(n: Int): List<ReceiptData>
    suspend fun saveProduct(receipt: ReceiptData?) : Boolean
    suspend fun saveAllProducts(receipt: List<ReceiptData>) : Boolean
}

