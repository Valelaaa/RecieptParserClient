package md.keeproblems.recieptparser.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import md.keeproblems.recieptparser.data.apiservices.ParserApiService
import md.keeproblems.recieptparser.data.entities.PriceInfoDto
import md.keeproblems.recieptparser.data.entities.ProductBody
import md.keeproblems.recieptparser.data.entities.ReceiptResponseBody
import md.keeproblems.recieptparser.data.entities.parser.ReceiptMapper
import md.keeproblems.recieptparser.domain.models.Products
import md.keeproblems.recieptparser.domain.models.PriceInfo
import md.keeproblems.recieptparser.domain.repository.ReceiptRepository
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException
import javax.inject.Inject

internal class ReceiptRepositoryImpl @Inject constructor(
    private val mapper: ReceiptMapper,
    private val apiService: ParserApiService
) : ReceiptRepository {
    private val cachedProducts = MutableStateFlow<ReceiptResponseBody?>(null)
    override suspend fun getProducts(url: String): Result<Products> {
        println("!!! beforeGet!!!")
        val result = runCatching {
            val products = getCachedProducts(url)
            println("!!! products:${products}")
            mapper.mapProductList(products)
        }.onFailure {
            it.printStackTrace()
        }
        return result
    }

    override suspend fun getTotalPrice(url: String): Result<PriceInfo> {
        val result = runCatching {
            val products = getCachedProducts(url)
            mapper.mapTotalPrice(products)
        }
        return result
    }
    fun parseHtml(url: String): ReceiptResponseBody {
        val doc: Document = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
            .timeout(10_000)
            .get()
        println("+++ ${parsingProcess(doc)}")

        return parsingProcess(doc)
    }
    private suspend fun getCachedProducts(url: String): ReceiptResponseBody? {
        if (cachedProducts.value == null) {
            val parsed = parseHtml(url)
            cachedProducts.update { parsed }
        }
        return cachedProducts.value
    }

    fun parseHtmlString(html: String): ReceiptResponseBody {
        println("!!!! html:${html}")
        val doc = Jsoup.parse(html)
        println("+++ $ {parsingProcess(doc)}")
        return parsingProcess(doc)
    }

    private fun parsingProcess(doc: Element): ReceiptResponseBody {
        val form = doc.getElementById("newFormTest")
            ?: throw RuntimeException("Form not found")

        val pTexts = form.select("p")
            .map { it.text().trim() }
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

        val total = form.select("span:matchesOwn(^TOTAL$)")
            .firstOrNull()
            ?.nextElementSibling()
            ?.text()
            ?.trim()

        val date = form.select("span").firstOrNull {
            it.text().trim().uppercase().startsWith("DATA")
        }?.text()
            ?.replaceFirst("^DATA\\s*[:\\-]*\\s*".toRegex(RegexOption.IGNORE_CASE), "")
            ?.trim()

        val time = form.select("span").firstOrNull {
            it.text().trim().uppercase().startsWith("ORA")
        }?.text()
            ?.replaceFirst("^ORA\\s*[:\\-]*\\s*".toRegex(RegexOption.IGNORE_CASE), "")
            ?.trim()

        val fiscal = form.select("span:matchesOwn(^BON FISCAL)")
            .firstOrNull()
            ?.nextElementSibling()
            ?.text()
            ?.trim()

        val factory = form.select("span:matchesOwn(^NUMĂRUL FABRICĂRII)")
            .firstOrNull()
            ?.nextElementSibling()
            ?.text()
            ?.trim()

        return ReceiptResponseBody(
            companyInfo = companyInfo,
            products = products,
            totals = totals,
            total = total,
            date = date,
            time = time,
            fiscal = fiscal,
            factoryNumber = factory
        )
    }

    private fun Element.getProducts(): List<ProductBody> {
        val productDivs = this.select("div").filter { div ->
            val spans = div.select("span")
            spans.size == 2 &&
                    spans[0].text().trim().isNotEmpty() &&
                    !listOf(
                        "TOTAL",
                        "TVA",
                        "CARD",
                        "BRUT",
                        "DATA",
                        "BON FISCAL",
                        "NUMĂRUL FABRICĂRII",
                        "Info"
                    )
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
            }.getOrElse { ProductBody.EMPTY }
        }
    }

    private suspend fun fetchHtml(url: String): String = withContext(Dispatchers.IO) {
        val request = okhttp3.Request.Builder()
            .url(url)
            .get()
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36"
            )
            .build()

        OkHttpClient().newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("HTTP ${response.code}") as Throwable
            response.body?.string() ?: throw IOException("Empty body")
        }
    }

    companion object {
        private const val PRICE_DELIMITER = 'x'
    }
}