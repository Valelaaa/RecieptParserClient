package md.keeproblems.recieptparser.data.entities

import com.google.gson.annotations.SerializedName

data class ProductBody(
    @SerializedName("productName")
    val productName: String,
    @SerializedName("productDescription")
    val productDescription: String = "",
    @SerializedName("productPrice")
    val productPrice: PriceInfo,
) {
    companion object {
        val EMPTY = ProductBody(productName = "", productDescription = "", productPrice = PriceInfo.EMPTY)
    }
}

data class PriceInfo(
    val price: Double,
    val count: Double? = null
) {
    companion object {
        val EMPTY = PriceInfo(price = 0.0, count = null)
    }
}