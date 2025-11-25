package md.keeproblems.recieptparser.ui.common.atomic

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.TextRes
import md.keeproblems.recieptparser.utils.resolve
import md.keeproblems.recieptparser.utils.textResource

@Composable
fun TextAtom(
    text: TextRes,
    style: AppTextStyle,
    color: Color = MaterialTheme.colorScheme.scrim,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.resolve(),
        fontSize = style.textStyle.fontSize,
        fontWeight = style.textStyle.fontWeight,
        color = color,
        modifier = modifier
    )
}

@Preview
@Composable
private fun TitleTextPreview() {
    RecieptParserTheme {
        TextAtom(
            text = textResource("Header Text"),
            style = AppTextStyle.TitleLarge,
        )
    }
}
