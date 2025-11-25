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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import md.keeproblems.recieptparser.ui.common.atomic.TextAtom
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.AppCurrency
import md.keeproblems.recieptparser.utils.CurrencyLocation
import md.keeproblems.recieptparser.utils.TextRes
import md.keeproblems.recieptparser.utils.extentions.formatMoney
import md.keeproblems.recieptparser.utils.textResource


@Composable
fun SpendMoneyCard(
    title: TextRes,
    amount: Double,
    currency: AppCurrency,
    modifier: Modifier = Modifier
) {
    val isPrefix = remember {
        currency.location == CurrencyLocation.PREFIX
    }
    val cardAmount = remember { textResource(amount.formatMoney()) }

    Card(
        modifier = modifier.fillMaxWidth(), shape = Shapes().extraLarge, colors =
            CardDefaults.cardColors()
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
            Row {
                if (isPrefix) {
                    TextAtom(
                        text = currency.symbol,
                        style = AppTextStyle.DisplayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                TextAtom(
                    text = cardAmount,
                    style = AppTextStyle.DisplayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                if (!isPrefix) {
                    TextAtom(
                        text = title,
                        style = AppTextStyle.DisplayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun SpendMoneyCardPreview() {
    RecieptParserTheme {
        SpendMoneyCard(
            title = textResource("title"),
            amount = 300.0,
            currency = AppCurrency.Dollar
        )
    }
}
