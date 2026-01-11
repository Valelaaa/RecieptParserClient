package md.keeproblems.recieptparser.data.entities.parser.impl

import android.os.Build
import androidx.annotation.RequiresApi
import md.keeproblems.recieptparser.data.entities.ReceiptResponseBody
import md.keeproblems.recieptparser.data.entities.parser.ReceiptMapper
import md.keeproblems.recieptparser.domain.models.Product
import md.keeproblems.recieptparser.domain.models.ReceiptData
import md.keeproblems.recieptparser.domain.models.PriceInfo
import md.keeproblems.recieptparser.utils.parseDate
import md.keeproblems.recieptparser.utils.parseTime
import md.keeproblems.recieptparser.utils.service.DateTimeParserService
import javax.inject.Inject

const val USER_ID = "25a4a520-831f-47c5-8bbf-63e2f01062a5"

internal class ReceiptMapperImpl @Inject constructor(private val dateTimeParserService: DateTimeParserService) :
    ReceiptMapper {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun mapProductList(from: ReceiptResponseBody?): ReceiptData {
        val (taxesDescription, taxes) = parseTaxes(from?.totals?.first())
        return ReceiptData(
            companyName = from?.companyInfo?.first() ?: "",
            products = from?.products?.map {
                Product(
                    productPrice = PriceInfo(it.productPrice.price.toString()),
                    productName = it.productName
                )
            } ?: emptyList(),
            priceInfo = mapTotalPrice(from),
            taxes = taxes ?: 0.0,
            taxesInfo = taxesDescription ?: "",
            receiptDate = parseDate(from?.date),
            receiptTime = parseTime(from?.time),
            id = USER_ID + dateTimeParserService.toTimestampMillis(
                from?.date ?: "",
                from?.time ?: ""
            )
        )
    }

    private fun parseTaxes(totals: String?): Pair<String?, Double?> {
        val taxDescription = totals?.substringBefore("->")
        val tax = totals?.substringAfter("-> ")?.toDoubleOrNull()
        return Pair(taxDescription, tax)
    }

    override fun mapTotalPrice(from: ReceiptResponseBody?): PriceInfo {
        return PriceInfo(from?.total ?: "0.0")
    }
}