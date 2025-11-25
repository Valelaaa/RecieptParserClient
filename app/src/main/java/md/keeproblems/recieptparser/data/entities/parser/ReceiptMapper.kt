package md.keeproblems.recieptparser.data.entities.parser

import md.keeproblems.recieptparser.data.entities.ReceiptResponseBody
import md.keeproblems.recieptparser.domain.models.Product
import md.keeproblems.recieptparser.domain.models.TotalPrice

internal interface ReceiptMapper {
    fun mapProductList(from: ReceiptResponseBody?): List<Product>
    fun mapTotalPrice(from: ReceiptResponseBody?): TotalPrice
}
