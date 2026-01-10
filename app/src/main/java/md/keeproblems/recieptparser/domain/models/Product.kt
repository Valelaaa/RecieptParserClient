package md.keeproblems.recieptparser.domain.models

internal data class Product(
    val productName: String,
    val productPrice: PriceInfo,
)

internal data class Products(
    val companyName:String,
    val products: List<Product>,
    val priceInfo: PriceInfo,
)