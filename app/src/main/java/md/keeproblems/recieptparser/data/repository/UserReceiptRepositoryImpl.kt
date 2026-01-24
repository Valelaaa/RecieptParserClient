package md.keeproblems.recieptparser.data.repository

import md.keeproblems.recieptparser.data.local.LocalSharedPrefsDataSource
import md.keeproblems.recieptparser.domain.models.ReceiptData
import javax.inject.Inject

internal class UserReceiptRepositoryImpl @Inject constructor(
    private val localDataSource: LocalSharedPrefsDataSource<ReceiptData>
) : UserReceiptRepository {

    override suspend fun getProductsById(id: String): ReceiptData? {
        return localDataSource.getAll().firstOrNull { it.id == id }
    }

    override suspend fun getFirstNProducts(n: Int): List<ReceiptData> {
        return localDataSource.getAll()
            .sortedWith(compareByDescending<ReceiptData> { it.receiptDate }
                .thenByDescending { it.receiptTime })
            .take(n)
    }

    override suspend fun saveProduct(receipt: ReceiptData?): Boolean {
        return if (receipt != null) localDataSource.save(receipt) else false
    }

    override suspend fun saveAllProducts(receipt: List<ReceiptData>): Boolean {
        return localDataSource.saveAll(receipt)
    }

    override suspend fun getAllProducts(): List<ReceiptData> {
        return localDataSource.getAll()
            .sortedWith(
                compareByDescending<ReceiptData> { it.receiptDate }
                    .thenByDescending { it.receiptTime }
            )
    }
}
