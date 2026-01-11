package md.keeproblems.recieptparser.ui.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import md.keeproblems.recieptparser.R
import md.keeproblems.recieptparser.domain.models.ReceiptData
import md.keeproblems.recieptparser.ui.common.atomic.TextAtom
import md.keeproblems.recieptparser.ui.common.buttons.PrimaryButton
import md.keeproblems.recieptparser.ui.common.cards.SpendMoneyCard
import md.keeproblems.recieptparser.ui.common.cards.SpendMoneyCardShort
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.ImageRes
import md.keeproblems.recieptparser.utils.RenderIcon
import md.keeproblems.recieptparser.utils.TextRes
import md.keeproblems.recieptparser.utils.formatReceiptDate
import md.keeproblems.recieptparser.utils.formatTime
import md.keeproblems.recieptparser.utils.imageResource
import md.keeproblems.recieptparser.utils.textResource


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardScreen(
    onScanClick: () -> Unit,
    onSettingsClick: () -> Unit = {},
    onViewAllClick: () -> Unit = {},
    onReceiptClick: (String) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>(),
    scanResultFlow: MutableSharedFlow<Unit>,
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(scanResultFlow) {
        scanResultFlow.collect {
            viewModel.refreshHistory()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextAtom(
                        textResource("Expense Tracker"),
                        style = AppTextStyle.DisplayMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = MaterialTheme.colorScheme.background),
            )
        }, bottomBar = {
            PrimaryButton(
                onClick = onScanClick,
                textResource("Scan receipt"),
                contentModifier = Modifier.fillMaxWidth(),
                modifier = Modifier
                    .padding(24.dp)
                    .padding(bottom = 40.dp)
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                SpendMoneyCard(textResource("Total"), state.spentTotal)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SpendMoneyCardShort(
                        title = textResource("This month"),
                        price = state.spentMonth,
                        modifier = Modifier.weight(1f)
                    )
                    SpendMoneyCardShort(
                        title = textResource("This week"),
                        price = state.spentWeek,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            CategoriesSection()
            if (state.history.isNotEmpty())
                LastReceiptsSection(state.history, onViewAllClick = onViewAllClick, onReceiptClick)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun LastReceiptsSection(
    productList: List<ReceiptData>,
    onViewAllClick: () -> Unit,
    onReceiptClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextAtom(
                textResource("History"),
                style = AppTextStyle.TitleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onViewAllClick)
                    .padding(horizontal = 4.dp)
            ) {
                TextAtom(
                    textResource("View all"),
                    style = AppTextStyle.LabelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Card(
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.surface
            ), modifier = Modifier,
            shape = RoundedCornerShape(16.dp)
        ) {
            productList.forEachIndexed { index, item ->
                Column(modifier = Modifier.padding(12.dp)) {
                    ReceiptPreview(item, {
                        onReceiptClick(item.id)
                    })
                }
                if (productList.size - 1 != index)
                    HorizontalDivider()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun ReceiptPreview(receipt: ReceiptData, onClick: () -> Unit) {
    val receiptDate = formatReceiptDate(receipt.receiptDate)
    val receiptTime = formatTime(receipt.receiptTime)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(end = 24.dp)
                .weight(1f)
        ) {
            TextAtom(
                textResource(receipt.companyName),
                style = AppTextStyle.LabelMediumSemiBold,
                color = MaterialTheme.colorScheme.primary,
                softWrap = true,
                maxLines = 2
            )
            TextAtom(
                textResource("$receiptDate, $receiptTime"),
                style = AppTextStyle.LabelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        TextAtom(
            textResource(receipt.priceInfo.toString()),
            style = AppTextStyle.LabelMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }

}

@Composable
fun CategoriesSection() {
    val cardsArrangement = remember { 10.dp }
    val scrollState = rememberScrollState()
    val cards = listOf(
        CardData("Food", R.drawable.cutlery, MaterialTheme.colorScheme.secondary),
        CardData("Shopping", R.drawable.online_shopping),
        CardData("Subscriptions", R.drawable.subscribe, MaterialTheme.colorScheme.background),
        CardData("Transport", R.drawable.car)
    )
    val density = LocalDensity.current
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextAtom(
            textResource("Categories"),
            style = AppTextStyle.TitleMedium,
            color = MaterialTheme.colorScheme.primary
        )
        var maxCardWidth by remember { mutableStateOf(0.dp) }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(cardsArrangement),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(cards.size) { index ->
                val card = cards[index]
                CategoryCard(
                    title = textResource(card.title),
                    image = imageResource(card.imageRes),
                    imageBackground = card.imageBackground ?: MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .clickable { }
                        .onGloballyPositioned { coords ->
                            val widthDp = with(density) { coords.size.width.toDp() }
                            if (widthDp > maxCardWidth) maxCardWidth = widthDp
                        }
                        .width(maxCardWidth.takeIf { it > 0.dp } ?: Dp.Unspecified)
                )
            }
        }
    }
}

data class CardData(
    val title: String,
    val imageRes: Int,
    val imageBackground: Color? = null
)

@Composable
fun CategoryCard(
    title: TextRes,
    image: ImageRes,
    imageBackground: Color = Color.Unspecified,
    color: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = Shapes().large
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(color = imageBackground, shape = Shapes().medium)
                    .padding(7.dp)
            ) {
                image.RenderIcon(modifier = Modifier.size(32.dp), color = color)
            }
            TextAtom(
                text = title,
                style = AppTextStyle.Custom(
                    AppTextStyle.BodyMedium.textStyle.copy(fontWeight = FontWeight.SemiBold)
                ),
                color = color
            )
        }
    }
}


@Preview
@Composable
private fun CategoriesSectionPreview() {
    CategoriesSection()
}

@Preview
@Composable
private fun CategoryCardPreview() {
    CategoryCard(textResource("example"), imageResource(R.drawable.cutlery))
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DashboardScreenPreview() {
    RecieptParserTheme() {
        DashboardScreen(onScanClick = {}, scanResultFlow = MutableSharedFlow(), onReceiptClick = {})
    }
}