package md.keeproblems.recieptparser.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import md.keeproblems.recieptparser.R
import md.keeproblems.recieptparser.ui.common.atomic.TextAtom
import md.keeproblems.recieptparser.ui.common.buttons.PrimaryButton
import md.keeproblems.recieptparser.ui.common.cards.SpendMoneyCard
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.AppCurrency
import md.keeproblems.recieptparser.utils.ImageRes
import md.keeproblems.recieptparser.utils.RenderIcon
import md.keeproblems.recieptparser.utils.TextRes
import md.keeproblems.recieptparser.utils.imageResource
import md.keeproblems.recieptparser.utils.textResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardScreen(onScanClick: () -> Unit, onSettingsClick: () -> Unit = {}) {
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
                SpendMoneyCard(textResource("Total"), 1420.00, AppCurrency.Dollar)
                SpendMoneyCard(textResource("This week"), 290.00, AppCurrency.Dollar)
            }

            CategoriesSection()

        }
    }
}


@Composable
fun CategoriesSection() {
    val cardsArrangement = remember { 10.dp }
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        TextAtom(
            textResource("Categories"),
            style = AppTextStyle.DisplaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Column(verticalArrangement = Arrangement.spacedBy(cardsArrangement)) {
            Row(horizontalArrangement = Arrangement.spacedBy(cardsArrangement)) {
                CategoryCard(
                    title = textResource("Food"),
                    image = imageResource(R.drawable.cutlery),
                    imageBackground = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
                CategoryCard(
                    title = textResource("Shopping"),
                    image = imageResource(R.drawable.online_shopping),
                    modifier = Modifier.weight(1f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(cardsArrangement)) {
                CategoryCard(
                    title = textResource("Subscriptions"),
                    image = imageResource(R.drawable.subscribe),
                    imageBackground = MaterialTheme.colorScheme.background,
                    modifier = Modifier.weight(1f)
                )
                CategoryCard(
                    title = textResource("Transport"),
                    image = imageResource(R.drawable.car),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

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
                    AppTextStyle.TitleSmall.textStyle.copy(fontWeight = FontWeight.SemiBold)
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


@Preview
@Composable
private fun DashboardScreenPreview() {
    RecieptParserTheme() {
        DashboardScreen(onScanClick = {})
    }
}