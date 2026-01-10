package md.keeproblems.recieptparser.data.entities.parser.impl

import md.keeproblems.recieptparser.data.entities.ReceiptResponseBody
import md.keeproblems.recieptparser.data.entities.parser.ReceiptMapper
import md.keeproblems.recieptparser.domain.models.Product
import md.keeproblems.recieptparser.domain.models.Products
import md.keeproblems.recieptparser.domain.models.PriceInfo
import javax.inject.Inject

internal class ReceiptMapperImpl @Inject constructor() : ReceiptMapper {
    override fun mapProductList(from: ReceiptResponseBody?): Products {
        return Products(
            companyName = from?.companyInfo?.first() ?: "",
            products = from?.products?.map {
                Product(
                    productPrice = PriceInfo(it.productPrice.price.toString()),
                    productName = it.productName
                )
            } ?: emptyList(),
            priceInfo = mapTotalPrice(from)
        )
    }

    override fun mapTotalPrice(from: ReceiptResponseBody?): PriceInfo {
        return PriceInfo(from?.total ?: "0.0")
    }
}