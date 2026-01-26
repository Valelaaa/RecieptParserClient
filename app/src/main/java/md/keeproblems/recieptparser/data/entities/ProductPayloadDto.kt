package md.keeproblems.recieptparser.data.entities

data class ProductPayloadDto(
    val products: List<ProductDescription>
)

data class ProductDescription(
    val id: String,
    val name: String
)

data class ProductPayload(
    val products: List<ProductDescription>
)