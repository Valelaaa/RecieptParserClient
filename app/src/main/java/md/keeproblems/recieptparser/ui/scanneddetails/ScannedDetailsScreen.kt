package md.keeproblems.recieptparser.ui.scanneddetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import md.keeproblems.recieptparser.domain.models.Product
import md.keeproblems.recieptparser.domain.models.PriceInfo
import md.keeproblems.recieptparser.ui.common.atomic.TextAtom
import md.keeproblems.recieptparser.ui.common.buttons.PrimaryButton
import md.keeproblems.recieptparser.ui.common.buttons.SecondaryButton
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.textResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiptDetails(
    products: List<Product>,
    totalAmount: PriceInfo,
    onBackClick: () -> Unit,
    moreOptionsClick: () -> Unit,
    date: String,
    companyName: String,
    count: Int,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            ReceiptDetailsTopBar(onBackClick = onBackClick, actionsClick = moreOptionsClick)
        },
        bottomBar = {
            ReceiptDetailsBottomBar(onSaveClick = onSaveClick, onShareClick = onShareClick)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            ReceiptDetailsContent(products, date, companyName, count, totalAmount = totalAmount)
        }
    }
}

@Composable
internal fun ReceiptDetailsContent(
    products: List<Product>,
    date: String,
    companyName: String,
    count: Int,
    totalAmount: PriceInfo,
) {
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        ReceiptDetailsHeader(date, companyName, count = count)
        ProductsDetails(products, totalAmount = totalAmount)
    }

}

@Composable
internal fun ProductsDetails(products: List<Product>, totalAmount: PriceInfo) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextAtom(
            text = textResource("Products"),
            style = AppTextStyle.BodyLargeBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            val finalProducts = products
                .map {
                    if (it.productName.contains("6463")) {
                        Product(it.productName.substringAfter("6463 "), it.productPrice)
                    } else if (it.productName.contains("1018016")) {
                        Product(it.productName.substringAfter("1018016 "), it.productPrice)
                    } else it
                }.groupBy { it.productName }
                .map { (name, group) ->
                    val count = group.size
                    val singlePrice = group.first().productPrice

                    if (count > 1) {
                        group.first().copy(
                            productDescription = "$count x $singlePrice",
                            productPrice = PriceInfo((singlePrice.value.toDouble() * count).toString())
                        )
                    } else {
                        group.first()
                    }
                }
            finalProducts.forEach {
                ProductDetails(it)
            }
        }
        HorizontalDivider(
            thickness = 3.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
        )
        TotalAmount(totalAmount)
        HorizontalDivider()
    }
}

@Composable
internal fun TotalAmount(totalAmount: PriceInfo) {
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
    ) {
        TextAtom(
            text = textResource("Total"),
            style = AppTextStyle.BodyLargeBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        TextAtom(
            text = textResource(totalAmount.toString()),
            style = AppTextStyle.BodyLargeBold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
internal fun ProductDetails(product: Product) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider()
        val halfWidth = (maxWidth + 48.dp) / 2
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 24.dp)
            ) {
                TextAtom(
                    text = textResource(product.productName),
                    style = AppTextStyle.BodyLargeSemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = Int.MAX_VALUE,
                    softWrap = true,
                    modifier = Modifier
                )
                if (product.productDescription.isNotBlank()) {
                    Column(
                        modifier = Modifier.width(halfWidth),
                        horizontalAlignment = Alignment.End
                    ) {
                        TextAtom(
                            text = textResource(product.productDescription),
                            style = AppTextStyle.BodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            TextAtom(
                text = textResource(product.productPrice.toString()),
                style = AppTextStyle.BodyLargeSemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }

}

@Composable
private fun getItemsCount(count: Int): String {
    return remember {
        val isExclusion = count % 100 in 11..14
        val isSingular = count % 10 == 1

        if (isSingular && !isExclusion) {
            "$count item"
        } else {
            "$count items"
        }
    }
}

@Composable
internal fun ReceiptDetailsHeader(
    date: String,
    companyName: String,
    count: Int,
) {
    val itemsLabel = getItemsCount(count)
    Column(modifier = Modifier.fillMaxWidth()) {
        TextAtom(
            text = textResource(date),
            style = AppTextStyle.BodyLargeBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            TextAtom(
                text = textResource(companyName),
                style = AppTextStyle.BodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextAtom(
                text = textResource(itemsLabel),
                style = AppTextStyle.BodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        HorizontalDivider(
            thickness = 3.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        )
    }
}

@Composable
internal fun ReceiptDetailsBottomBar(
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .padding(top = 16.dp)
    ) {
        PrimaryButton(
            text = textResource("Save"),
            onClick = onSaveClick,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
        )
        SecondaryButton(
            text = textResource("Share"),
            onClick = onShareClick,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReceiptDetailsTopBar(
    onBackClick: () -> Unit,
    actionsClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors()
            .copy(containerColor = MaterialTheme.colorScheme.background),
        title = {
            TextAtom(
                text = textResource("Receipt Details"),
                style = AppTextStyle.Custom(
                    custom =
                        AppTextStyle.TitleLarge
                            .textStyle.copy(fontWeight = FontWeight.Medium)
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    modifier = Modifier.size(40.dp),
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = actionsClick) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(90f)
                        .size(40.dp),
                )
            }
        }
    )
}

@Preview
@Composable
private fun ReceiptDetailsScreenPreview() {
    RecieptParserTheme {
        ReceiptDetails(
            listOf(
                Product(
                    productName = "productName",
                    productPrice = PriceInfo(
                        "350",
                    )
                ),
                Product(
                    productName = "productName",
                    productPrice = PriceInfo(
                        "350",
                    )
                ),
                Product(
                    productName = "productName",
                    productDescription = "3 x 15",
                    productPrice = PriceInfo(
                        "350",
                    )
                ),
                Product(
                    productName = "productName",
                    productPrice = PriceInfo(
                        "350",
                    )
                ),
            ),
            onBackClick = {},
            date = "Monday, Oct 27",
            companyName = "NR.1",
            count = 15,
            moreOptionsClick = {},
            onSaveClick = {},
            onShareClick = {},
            totalAmount = PriceInfo("3500")
        )
    }
}