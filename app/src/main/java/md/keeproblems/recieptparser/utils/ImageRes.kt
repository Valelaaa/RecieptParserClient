package md.keeproblems.recieptparser.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter

sealed class ImageRes {

    data class Drawable(@DrawableRes val id: Int) : ImageRes()

    data class Vector(val imageVector: ImageVector) : ImageRes()

    data class PainterImage(val painter: Painter) : ImageRes()

    data class BitmapImage(val bitmap: ImageBitmap) : ImageRes()

    data class RemoteUrl(val url: String) : ImageRes()

    data class FileImage(val path: String) : ImageRes()

    // optional: base64 support
    data class Base64Image(val base64: String) : ImageRes()
}

fun imageResource(@DrawableRes id: Int): ImageRes =
    ImageRes.Drawable(id)

fun imageResource(imageVector: ImageVector): ImageRes =
    ImageRes.Vector(imageVector)

fun imageResource(painter: Painter): ImageRes =
    ImageRes.PainterImage(painter)

fun imageResource(bitmap: Bitmap): ImageRes =
    ImageRes.BitmapImage(bitmap.asImageBitmap())

fun imageResource(url: String): ImageRes =
    ImageRes.RemoteUrl(url)

fun imageResourceFile(path: String): ImageRes =
    ImageRes.FileImage(path)



@Composable
fun ImageRes.Render(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    color: Color = Color.Unspecified,
    contentScale: ContentScale = ContentScale.Crop
) {
    when (this) {

        is ImageRes.Drawable -> {
            Image(
                painter = painterResource(id),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }

        is ImageRes.Vector -> {
            Image(
                imageVector = imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }

        is ImageRes.PainterImage -> {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }

        is ImageRes.BitmapImage -> {
            Image(
                bitmap = bitmap,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }

        is ImageRes.RemoteUrl -> {
            // COIL
            val painter = rememberAsyncImagePainter(url)
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }

        is ImageRes.FileImage -> {
            val painter = rememberAsyncImagePainter(path)
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }

        is ImageRes.Base64Image -> {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            Image(
                bitmap = bmp.asImageBitmap(),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = contentScale
            )
        }
    }
}

@Composable
fun ImageRes.RenderIcon(
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    color: Color = Color.Unspecified,
) {
    when (this) {

        is ImageRes.Drawable -> {
            Icon(
                painter = painterResource(id),
                contentDescription = contentDescription,
                modifier = modifier,
                tint = color
            )
        }

        is ImageRes.Vector -> {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = color

            )
        }

        is ImageRes.PainterImage -> {
            Icon(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = color

            )
        }

        is ImageRes.BitmapImage -> {
            Icon(
                bitmap = bitmap,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = color

            )
        }

        else -> {}
    }
}
