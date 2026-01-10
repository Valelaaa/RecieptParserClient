package md.keeproblems.recieptparser.data.entities

import com.google.gson.annotations.SerializedName

data class ProductBody(
    @SerializedName("productName")
    val productName: String,
    @SerializedName("productDescription")
    val productDescription: String = "",
    @SerializedName("productPrice")
    val productPrice: PriceInfoDto,
) {
    companion object {
        val EMPTY = ProductBody(productName = "", productDescription = "", productPrice = PriceInfoDto.EMPTY)
    }
}

data class PriceInfoDto(
    val price: Double,
    val count: Double? = null
) {
    companion object {
        val EMPTY = PriceInfoDto(price = 0.0, count = null)
    }
}