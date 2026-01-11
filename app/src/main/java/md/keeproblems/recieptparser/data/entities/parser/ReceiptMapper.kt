package md.keeproblems.recieptparser.data.entities.parser

import md.keeproblems.recieptparser.data.entities.ReceiptResponseBody
import md.keeproblems.recieptparser.domain.models.ReceiptData
import md.keeproblems.recieptparser.domain.models.PriceInfo

internal interface ReceiptMapper {
    fun mapProductList(from: ReceiptResponseBody?): ReceiptData
    fun mapTotalPrice(from: ReceiptResponseBody?): PriceInfo
}
