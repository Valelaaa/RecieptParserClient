package md.keeproblems.recieptparser.ui.common.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import md.keeproblems.recieptparser.ui.common.atomic.TextAtom
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.TextRes
import md.keeproblems.recieptparser.utils.textResource

@Composable
internal fun SecondaryButton(
    onClick: () -> Unit,
    text: TextRes,
    shape: Shape = Shapes().large,
    border: BorderStroke = BorderStroke(
        width = 2.dp,
        color = MaterialTheme.colorScheme.primary
    ),
    contentModifier: Modifier = Modifier,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        shape = shape,
        modifier = modifier,
        border = border,
    ) {
        Column(
            modifier = contentModifier.padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextAtom(
                text = text,
                style = AppTextStyle.Custom(
                    AppTextStyle.TitleSmall.textStyle.copy(fontWeight = FontWeight.SemiBold)
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    RecieptParserTheme() {
        SecondaryButton(
            onClick = { },
            text = textResource("Details"),
            shape = Shapes().extraLarge,
            contentModifier = Modifier,
            modifier = Modifier
        )
    }
}
