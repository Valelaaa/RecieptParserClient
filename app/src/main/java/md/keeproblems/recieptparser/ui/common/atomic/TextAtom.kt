package md.keeproblems.recieptparser.ui.common.atomic

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
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
    textAlign: TextAlign = TextAlign.Unspecified,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Visible,
    softWrap: Boolean = true,
    lineHeight: TextUnit = TextUnit.Unspecified,
    modifier: Modifier = Modifier
) {
    Text(
        text = text.resolve(),
        fontSize = style.textStyle.fontSize,
        fontWeight = style.textStyle.fontWeight,
        textAlign = textAlign,
        color = color,
        maxLines = maxLines,
        modifier = modifier,
        overflow = overflow,
        softWrap = softWrap,
        lineHeight = lineHeight,
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
