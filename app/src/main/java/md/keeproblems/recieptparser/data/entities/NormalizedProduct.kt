package md.keeproblems.recieptparser.data.entities

data class NormalizedProduct(
    val id: String,
    val originalName: String,
    val normalizedName: String,
    val category: String,
    val brand: String,
    val size: String,
    val flavor: String,
    val categoryColor: String
)