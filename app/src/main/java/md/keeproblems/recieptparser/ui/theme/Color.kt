package md.keeproblems.recieptparser.ui.theme

import androidx.compose.ui.graphics.Color


// Neutrals
// Общий фон (подложка экрана, почти белый, но не чистый)
val NeutralBackground = Color(0xFFF3F4F6) // было F5F5F7, делаем чуть теплее и светлее

// Карточки, белые блоки
val NeutralSurface    = Color(0xFFFFFFFF)

// Светло-серые карточки внутри (Total / This week / категории)
val NeutralSurfaceAlt = Color(0xFFF1F2F4) // вместо E5E7EB, там гораздо более светлый серый

// Бордеры, дивайдеры, outline кнопки
val NeutralOutline    = Color(0xFFE0E1E5) // твой был сильно насыщен (D1D5DB)

// Текст
val NeutralOnSurface  = Color(0xFF111111) // почти чистый чёрный как на макете
val NeutralSecondaryText = Color(0xFF3A3A3B) // типичный iOS серый для вторичного текста

// Primary = чёрные кнопки как в макетах

// Accent green (успешно отсканировано, категории Food)
val AccentGreenContainer = Color(0xFFE5F6D6)

// иконка вилки и ножа / галочка
val AccentGreen          = Color(0xFFC0DCAB)

// текст поверх зелёного (у тебя там иконка, но на всякий)
val OnAccentGreen        = Color(0xFF0F172A)

// Accent teal (углы сканера QR)
val AccentTeal        = Color(0xFF5ED3D0)
val OnAccentTeal      = Color(0xFF022626)
val PrimaryMain   = Color(0xFF000000)
val OnPrimaryMain = Color(0xFFFFFFFF)
// Warm pastel (бежевый фон третьего экрана)
val WarmBackground    = Color(0xFFF8EBD8)
val WarmChip          = Color(0xFFEFD7BD)

// Chart colors / доп-акценты
val ChartFood         = Color(0xFFA3D977)
val ChartShopping     = Color(0xFFF8B4C4)
val ChartSubs         = Color(0xFFC3B5F5)
val ChartTransport    = Color(0xFF8ED1D4)

// Error (стандартный, почти не видно в макетах, но пусть будет)
val ErrorRed          = Color(0xFFB3261E)
val OnErrorRed        = Color(0xFFFFFFFF)
