package md.keeproblems.recieptparser.ui.common.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
fun PrimaryButton(
    onClick: () -> Unit,
    text: TextRes,
    shape: Shape = Shapes().large,
    contentModifier: Modifier = Modifier,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = shape,
        modifier = modifier
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
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview
@Composable
private fun PrimaryButtonPreview() {
    RecieptParserTheme() {
        PrimaryButton(onClick = {}, textResource("scan reciept"))
    }
}