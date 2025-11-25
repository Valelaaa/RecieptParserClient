package md.keeproblems.recieptparser.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class TextRes {
    internal data class Id(
        @StringRes val id: Int,
        val args: List<Any> = emptyList()
    ) : TextRes()

    internal data class Raw(
        val value: String
    ) : TextRes()
}

fun textResource(@StringRes id: Int, vararg args: Any): TextRes =
    TextRes.Id(id, args.toList())

fun textResource(string: String): TextRes =
    TextRes.Raw(string)

@Composable
fun TextRes.resolve(): String = when (this) {
    is TextRes.Id -> stringResource(id, *args.toTypedArray())
    is TextRes.Raw -> value
}