package md.keeproblems.recieptparser.data.entities.parser.impl

import md.keeproblems.recieptparser.data.entities.ReceiptResponseBody
import md.keeproblems.recieptparser.data.entities.parser.ReceiptMapper
import md.keeproblems.recieptparser.domain.models.Product
import md.keeproblems.recieptparser.domain.models.TotalPrice

internal class ReceiptMapperImpl : ReceiptMapper {
    override fun mapProductList(from: ReceiptResponseBody?): List<Product> {
        return from?.products?.map {
            Product(
                productPrice = it.productPrice.price.toString(),
                productName = it.productName
            )
        } ?: emptyList()
    }

    override fun mapTotalPrice(from: ReceiptResponseBody?): TotalPrice {
        return TotalPrice(from?.total ?: "0.0")
    }
}