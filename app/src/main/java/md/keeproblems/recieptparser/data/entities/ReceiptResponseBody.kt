package md.keeproblems.recieptparser.data.entities

import com.google.gson.annotations.SerializedName

internal data class ReceiptResponseBody(
    @SerializedName("companyInfo")
    val companyInfo: List<String>,
    @SerializedName("products")
    val products: List<ProductBody>,
    @SerializedName("totals")
    val totals: List<String>,
    @SerializedName("total")
    val total: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("fiscal")
    val fiscal: String?,
    @SerializedName("factoryNumber")
    val factoryNumber: String?,
) {
    override fun toString(): String {
        return buildString {
            appendLine("Company Info:")
            companyInfo.forEach { appendLine("  $it") }

            appendLine("\nProducts:")
            products.forEach { appendLine("  $it") }

            appendLine("\nTotals:")
            totals.forEach { appendLine("  $it") }

            appendLine("\nTOTAL: $total")
            appendLine("DATE: $date")
            appendLine("TIME: $time")
            appendLine("BON FISCAL: $fiscal")
            appendLine("Factory Number: $factoryNumber")
        }
    }

}
