package md.keeproblems.recieptparser.data.mock

import md.keeproblems.recieptparser.data.entities.PriceInfoDto
import md.keeproblems.recieptparser.data.entities.ProductBody
import md.keeproblems.recieptparser.data.entities.ReceiptResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

internal class ClientParser {
    companion object {
        private const val PRICE_DELIMITER = 'x'
    }

    fun parseHtml(url: String): ReceiptResponseBody {
        val doc: Document = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
            .timeout(10_000)
            .get()
        return parsingProcess(doc)
    }

    private fun parsingProcess(doc: Element): ReceiptResponseBody {
        val form = doc.getElementById("newFormTest") ?: throw RuntimeException("Form not found")

        val pTexts = form.select("p").map { it.text().trim() }
            .filter { it.isNotEmpty() && !it.contains("````") }
        val companyInfo = pTexts.take(4)


        val products = form.getProducts()

        val totalsDivs = form.select("div").filter { div ->
            val spans = div.select("span")
            spans.size == 2 && listOf("BRUT", "TVA").any { div.text().contains(it) }
        }

        val totals = totalsDivs.map { div ->
            val spans = div.select("span")
            "${spans[0].text().trim()} -> ${spans[1].text().trim()}"
        }

        val total = form.select("span:matchesOwn(^TOTAL$)").firstOrNull()?.nextElementSibling()?.text()?.trim()

        // --- Дата и время (DATA / ORA) ---
        val rawDateSpan = form.select("span").firstOrNull { it.text().trim().uppercase().startsWith("DATA") }
        val date = rawDateSpan?.text()
            ?.replaceFirst("^DATA\\s*[:\\-]*\\s*".toRegex(RegexOption.IGNORE_CASE), "")?.trim()

        val rawTimeSpan = form.select("span").firstOrNull { it.text().trim().uppercase().startsWith("ORA") }
        val time = rawTimeSpan?.text()
            ?.replaceFirst("^ORA\\s*[:\\-]*\\s*".toRegex(RegexOption.IGNORE_CASE), "")?.trim()


        val fiscalSpan = form.select("span:matchesOwn(^BON FISCAL)").firstOrNull()?.nextElementSibling()?.text()?.trim()
        val factorySpan =
            form.select("span:matchesOwn(^NUMĂRUL FABRICĂRII)").firstOrNull()?.nextElementSibling()?.text()?.trim()

        return ReceiptResponseBody(
            companyInfo = companyInfo,
            products = products,
            totals = totals,
            total = total,
            date = date,
            time = time,
            fiscal = fiscalSpan,
            factoryNumber = factorySpan,
        )
    }


    private fun Element.getProducts(): List<ProductBody> {
        val productDivs = this.select("div").filter { div ->
            val spans = div.select("span")
            spans.size == 2 && spans[0].text().trim().isNotEmpty() &&
                    !listOf("TOTAL", "TVA", "CARD", "BRUT", "DATA", "BON FISCAL", "NUMĂRUL FABRICĂRII", "Info")
                        .any { spans[0].text().contains(it) }
        }

        return productDivs.map { div ->
            val spans = div.select("span")
            runCatching {
                val priceInfo = spans[1].text().trim().split(PRICE_DELIMITER).map { it.trim() }
                ProductBody(
                    productName = spans[0].text().trim(),
                    productPrice = PriceInfoDto(
                        count = priceInfo.first().toDouble(),
                        price = priceInfo.last().toDouble()
                    )
                )
            }.onFailure {
                it.printStackTrace()
            }.getOrElse { ProductBody.EMPTY }
        }
    }
}