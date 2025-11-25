package md.keeproblems.recieptparser.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightAppColorScheme = lightColorScheme(
    primary = PrimaryMain,
    onPrimary = OnPrimaryMain,

    primaryContainer = Color(0xFF000000),   // если вообще не юзаешь, можно забить
    onPrimaryContainer = Color(0xFFFFFFFF),

    secondary = AccentGreen,
    onSecondary = OnAccentGreen,
    secondaryContainer = AccentGreenContainer,
    onSecondaryContainer = OnAccentGreen,

    background = NeutralBackground,         // общий фон (как края экрана)
    onBackground = NeutralOnSurface,

    surface = NeutralSurface,               // основные карточки (белые)
    onSurface = NeutralOnSurface,

    surfaceVariant = NeutralSurfaceAlt,     // лёгкие серые подложки
    onSurfaceVariant = NeutralSecondaryText,

    outline = NeutralOutline,
    outlineVariant = NeutralOutline,

    error = ErrorRed,
    onError = OnErrorRed,
    errorContainer = Color(0xFFFCE4E4),
    onErrorContainer = ErrorRed,

    scrim = Color(0x66000000),

    // ВАЖНО: убираем чёрный tint, иначе всё сереет при elevation
    surfaceTint = Color.Unspecified
)


val DarkAppColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF000000),
    primaryContainer = Color(0xFF111827),
    onPrimaryContainer = Color(0xFFF9FAFB),

    secondary = AccentGreen,
    onSecondary = Color(0xFF04120A),
    secondaryContainer = Color(0xFF2F3F24),
    onSecondaryContainer = Color(0xFFE4F4D0),

    tertiary = AccentTeal,
    onTertiary = Color(0xFF002022),
    tertiaryContainer = Color(0xFF0D3C3F),
    onTertiaryContainer = Color(0xFFD9F4F4),

    background = Color(0xFF050608),
    onBackground = Color(0xFFF9FAFB),

    surface = Color(0xFF0B0F15),
    onSurface = Color(0xFFF9FAFB),
    surfaceVariant = Color(0xFF111827),
    onSurfaceVariant = NeutralSecondaryText,

    outline = Color(0xFF374151),
    outlineVariant = Color(0xFF111827),

    error = ErrorRed,
    onError = OnErrorRed,
    errorContainer = Color(0xFF410002),
    onErrorContainer = Color(0xFFFFB4AB),

    scrim = Color(0x99000000),
    surfaceTint = Color(0xFFFFFFFF),
)

@Composable
fun RecieptParserTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkAppColorScheme
        else -> LightAppColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}