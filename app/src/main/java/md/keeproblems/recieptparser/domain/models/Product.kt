package md.keeproblems.recieptparser.domain.models

import kotlinx.serialization.Serializable
import md.keeproblems.recieptparser.data.local.WithId
import md.keeproblems.recieptparser.utils.LocalDateSerializer
import md.keeproblems.recieptparser.utils.LocalTimeSerializer
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Serializable
internal data class Product(
    val productName: String,
    val productPrice: PriceInfo,
    val productDescription: String = "",
    val category: Category = Category()
)

@Serializable
internal data class Category(val name: String = "", val colorValue: String = "")

@Serializable
internal data class ReceiptData(
    val companyName: String,
    val products: List<Product>,
    val priceInfo: PriceInfo,
    @Serializable(with = LocalDateSerializer::class)
    val receiptDate: LocalDate? = null,
    @Serializable(with = LocalTimeSerializer::class)
    val receiptTime: LocalTime? = null,
    val taxesInfo: String = "",
    val taxes: Double = 0.0,
    override val id: String = UUID.randomUUID().toString()
) : WithId