package md.keeproblems.recieptparser.ui.common.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import md.keeproblems.recieptparser.domain.models.PriceInfo
import md.keeproblems.recieptparser.ui.common.atomic.TextAtom
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.TextRes
import md.keeproblems.recieptparser.utils.textResource


@Composable
internal fun SpendMoneyCard(
    title: TextRes,
    price: PriceInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = Shapes().extraLarge,
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextAtom(
                text = title,
                style = AppTextStyle.Custom(
                    AppTextStyle.TitleSmall.textStyle.copy(fontWeight = FontWeight.SemiBold)
                ),
                color = MaterialTheme.colorScheme.primary
            )
            TextAtom(
                text = textResource(price.toString()),
                style = AppTextStyle.DisplayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
internal fun SpendMoneyCardShort(
    title: TextRes,
    price: PriceInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = Shapes().extraLarge,
        colors = CardDefaults.cardColors()
            .copy(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextAtom(
                text = title,
                style = AppTextStyle.Custom(
                    AppTextStyle.TitleSmall.textStyle.copy(fontWeight = FontWeight.SemiBold)
                ),
                color = MaterialTheme.colorScheme.primary
            )
            TextAtom(
                softWrap = false,
                text = textResource(price.toString()),
                style = AppTextStyle.TitleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Preview
@Composable
private fun SpendMoneyCardPreview() {
    RecieptParserTheme {
        Column {
            SpendMoneyCard(
                title = textResource("title"),
                PriceInfo("300")
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SpendMoneyCardShort(
                    title = textResource("title"),
                    PriceInfo("300"),
                    modifier = Modifier.weight(1f)
                )
                SpendMoneyCardShort(
                    title = textResource("title"),
                    PriceInfo("300"),
                    modifier = Modifier.weight(1f)
                )
            }

        }
    }
}
