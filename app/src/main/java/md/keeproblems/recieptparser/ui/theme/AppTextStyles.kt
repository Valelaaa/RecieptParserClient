package md.keeproblems.recieptparser.ui.theme

import InterFontFamily
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


sealed class AppTextStyle(val textStyle: TextStyle) {

    // "Expense Tracker", "Scan receipt"
    data object DisplayLarge : AppTextStyle(
        TextStyle(
            fontSize = 38.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 46.sp,
            fontFamily = InterFontFamily
        )
    )

    // "$1,420.00", "$290.00"
    data object DisplayMedium : AppTextStyle(
        TextStyle(
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 42.sp,
            fontFamily = InterFontFamily
        )
    )

    data object DisplaySmall : AppTextStyle(
        TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 36.sp,
            fontFamily = InterFontFamily
        )
    )

    // "Successfully scanned!", "Categories"
    data object TitleLarge : AppTextStyle(
        TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 30.sp,
            fontFamily = InterFontFamily
        )
    )

    data object TitleMedium : AppTextStyle(
        TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 26.sp,
            fontFamily = InterFontFamily
        )
    )

    data object TitleSmall : AppTextStyle(
        TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            fontFamily = InterFontFamily
        )
    )

    // обычный текст
    data object BodyLarge : AppTextStyle(
        TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 22.sp,
            fontFamily = InterFontFamily
        )
    )

    data object BodyMedium : AppTextStyle(
        TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.sp,
            fontFamily = InterFontFamily
        )
    )

    data object BodySmall : AppTextStyle(
        TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 18.sp,
            fontFamily = InterFontFamily
        )
    )

    // подписи / кнопки
    data object LabelLarge : AppTextStyle(
        TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.sp,
            fontFamily = InterFontFamily
        )
    )

    data object LabelMedium : AppTextStyle(
        TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp,
            fontFamily = InterFontFamily
        )
    )

    data object LabelSmall : AppTextStyle(
        TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp,
            fontFamily = InterFontFamily
        )
    )

    data class Custom(val custom: TextStyle) : AppTextStyle(custom)
}