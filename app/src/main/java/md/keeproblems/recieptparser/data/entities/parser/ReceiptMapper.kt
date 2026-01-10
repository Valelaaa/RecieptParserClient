package md.keeproblems.recieptparser.data.entities.parser

import md.keeproblems.recieptparser.data.entities.ReceiptResponseBody
import md.keeproblems.recieptparser.domain.models.Products
import md.keeproblems.recieptparser.domain.models.PriceInfo

internal interface ReceiptMapper {
    fun mapProductList(from: ReceiptResponseBody?): Products
    fun mapTotalPrice(from: ReceiptResponseBody?): PriceInfo
}
