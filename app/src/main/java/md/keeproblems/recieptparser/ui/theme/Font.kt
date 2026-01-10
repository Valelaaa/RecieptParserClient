
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import md.keeproblems.recieptparser.R

import androidx.compose.ui.text.font.Font

val OpenSans = FontFamily(
    // Regular & Italic
    Font(R.font.open_sans_regular, FontWeight.Normal),
    Font(R.font.open_sans_italic, FontWeight.Normal, FontStyle.Italic),

    // Light
    Font(R.font.open_sans_light, FontWeight.Light),
    Font(R.font.open_sans_light_italic, FontWeight.Light, FontStyle.Italic),

    // Medium
    Font(R.font.open_sans_medium, FontWeight.Medium),
    Font(R.font.open_sans_medium_italic, FontWeight.Medium, FontStyle.Italic),

    // SemiBold
    Font(R.font.open_sans_semi_bold, FontWeight.SemiBold),
    Font(R.font.open_sans_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),

    // Bold
    Font(R.font.open_sans_bold, FontWeight.Bold),
    Font(R.font.open_sans_bold_italic, FontWeight.Bold, FontStyle.Italic),

    // Extra Bold
    Font(R.font.open_sans_extra_bold, FontWeight.ExtraBold),
    Font(R.font.open_sans_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic)
)

val OpenSansCondensed = FontFamily(
    Font(R.font.open_sans_condensed_regular, FontWeight.Normal),
    Font(R.font.open_sans_condensed_bold, FontWeight.Bold),
    // ... добавь остальные из своего списка аналогично
)

