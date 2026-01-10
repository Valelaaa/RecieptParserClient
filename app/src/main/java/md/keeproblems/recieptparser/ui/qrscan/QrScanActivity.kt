package md.keeproblems.recieptparser.ui.qrscan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import md.keeproblems.recieptparser.ui.common.atomic.TextAtom
import md.keeproblems.recieptparser.ui.scanneddetails.ReceiptDetails
import md.keeproblems.recieptparser.ui.scansuccess.SuccessScanResult
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.textResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalGetImage::class)
class QrScanActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    private fun startCamera() {
        setContent {
            var scannedQr by remember { mutableStateOf<String?>(null) }
            val viewModel = viewModels<QrScanViewModel>().value
            RecieptParserTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (scannedQr == null) {
                        AndroidView(
                            factory = { ctx ->
                                prepareScan(
                                    ctx,
                                    scannedQr
                                ) {
                                    scannedQr = it
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                        QRScanView()
                    }

                    scannedQr?.let { qr ->
                        println("!!! qr: $qr")
                        ShowProducts(viewModel, qr)
                    }
                }
            }
        }
    }

    @Composable
    private fun BoxScope.QRScanView() {
        val boxSizeDp: Dp = 200.dp
        val boxSizePx = with(LocalDensity.current) {
            boxSizeDp.toPx()
        }
        ScannerOverlay(
            overlayOpacity = 0.20,
            boxSizeDp = boxSizeDp,

            )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = (boxSizePx.dp / 2) + 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextAtom(
                text = textResource("Point the camera at the QR code"),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                style = AppTextStyle.BodyLarge
            )
            TextAtom(
                text = textResource("The receipt will be processed automatically"),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                style = AppTextStyle.BodyMedium
            )
        }
    }

    @Composable
    private fun ScannerOverlay(
        modifier: Modifier = Modifier,
        boxSizeDp: Dp = 250.dp,
        overlayOpacity: Double = 0.13,
        cornerRadiusDp: Dp = 16.dp,
        strokeWidthDp: Dp = 4.dp,
        lengthDp: Dp = 40.dp,
        cornerColor: Color = MaterialTheme.colorScheme.secondary,
    ) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val boxSize = boxSizeDp.toPx()
            val cornerRadius = cornerRadiusDp.toPx()
            val stroke = strokeWidthDp.toPx()
            val length = lengthDp.toPx()

            val left = (size.width - boxSize) / 2
            val top = (size.height - boxSize) / 2
            val right = left + boxSize
            val bottom = top + boxSize

            with(drawContext.canvas.nativeCanvas) {
                val checkpoint = saveLayer(null, null)

                drawRect(color = Color.Black.copy(alpha = overlayOpacity.toFloat()))
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = Offset(left, top),
                    size = Size(boxSize, boxSize),
                    cornerRadius = CornerRadius(cornerRadiusDp.toPx()),
                    blendMode = BlendMode.Clear,
                )

                restoreToCount(checkpoint)
            }
            val arcSize = Size(cornerRadius * 2, cornerRadius * 2)

            val corners = listOf(
                Path().apply {
                    moveTo(left, top + length)
                    lineTo(left, top + cornerRadius)
                    arcTo(Rect(Offset(left, top), arcSize), 180f, 90f, false)
                    lineTo(left + length, top)
                },
                Path().apply {
                    moveTo(right - length, top)
                    lineTo(right - cornerRadius, top)
                    arcTo(Rect(Offset(right - cornerRadius * 2, top), arcSize), 270f, 90f, false)
                    lineTo(right, top + length)
                },
                Path().apply {
                    moveTo(right, bottom - length)
                    lineTo(right, bottom - cornerRadius)
                    arcTo(
                        Rect(Offset(right - cornerRadius * 2, bottom - cornerRadius * 2), arcSize),
                        0f,
                        90f,
                        false
                    )
                    lineTo(right - length, bottom)
                },
                Path().apply {
                    moveTo(left + length, bottom)
                    lineTo(left + cornerRadius, bottom)
                    arcTo(Rect(Offset(left, bottom - cornerRadius * 2), arcSize), 90f, 90f, false)
                    lineTo(left, bottom - length)
                },
            )

            corners.forEach { path ->
                drawPath(
                    path = path,
                    color = cornerColor,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }

            drawRoundRect(
                color = Color.White.copy(alpha = 0.5f),
                topLeft = Offset(left, top),
                size = Size(boxSize, boxSize),
                cornerRadius = CornerRadius(16.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
            )
        }

    }

    @Composable
    private fun ShowProducts(viewModel: QrScanViewModel, qr: String) {
        val state by viewModel.state.collectAsState()
        val navController = rememberNavController()
        val currentDate = remember {
            val formatter =
                DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.ENGLISH)
            LocalDate.now().format(formatter)
        }
        LoadingContainer(isLoading = state.isLoading) {
            NavHost(
                navController = navController,
                startDestination = QrScanScreens.SuccessScreen.route
            ) {
                composable(route = QrScanScreens.SuccessScreen.route) {
                    if (state.products.isNotEmpty()) {

                        SuccessScanResult(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            date = currentDate,
                            priceInfo = state.priceInfo,
                            onSaveReceipt = {
                                navController.navigate(QrScanScreens.ScannedList.route)
                                viewModel.onSaveReceipt()
                            },
                            onShareReceipt = {
                                navController.navigate(QrScanScreens.ReceiptDetailsScreen.route)
                            }
                        )
                    }
                }
                composable(route = QrScanScreens.ScannedList.route) {
                    if (state.products.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.products.forEach {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                        .clip(MaterialTheme.shapes.medium)
                                        .padding(horizontal = 12.dp, vertical = 16.dp)
                                ) {
                                    Text(
                                        it.productName,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Text(
                                        text = it.productPrice.toString(),
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }
                }
                composable(route = QrScanScreens.ReceiptDetailsScreen.route) {
                    if (state.products.isNotEmpty()) {
                        ReceiptDetails(
                            products = state.products,
                            onBackClick = { navController.popBackStack() },
                            date = currentDate,
                            companyName = state.companyName,
                            count = state.products.size,
                            moreOptionsClick = {},
                            onSaveClick = {},
                            onShareClick = {}
                        )
                    }
                }
            }


        }
        LaunchedEffect(qr) {
            viewModel.updateProducts(url = qr)
        }
        LaunchedEffect(state.errorMessage) {
            Toast.makeText(this@QrScanActivity, state.errorMessage, Toast.LENGTH_SHORT)
                .show()
            viewModel.clearError()
        }
    }

    private fun prepareScan(
        ctx: Context,
        scannedQr: String?,
        updateScannedQr: (String) -> Unit
    ): PreviewView {
        val previewView = PreviewView(ctx)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            val scannerOptions = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = BarcodeScanning.getClient(scannerOptions)

            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImageProxy(scanner, imageProxy) { qr ->
                            if (scannedQr == null) updateScannedQr(qr)
                        }
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            previewView.post {
                preview.setSurfaceProvider(previewView.surfaceProvider)
                try {
                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                        this@QrScanActivity,
                        cameraSelector,
                        preview,
                        analysis
                    )
                    camera.cameraControl.startFocusAndMetering(
                        FocusMeteringAction.Builder(
                            previewView.meteringPointFactory.createPoint(
                                previewView.width / 2f,
                                previewView.height / 2f
                            ),
                            FocusMeteringAction.FLAG_AF
                        ).apply {
                            setAutoCancelDuration(5, TimeUnit.SECONDS)
                        }.build()
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }, ContextCompat.getMainExecutor(ctx))

        return previewView
    }

    @Composable
    fun LoadingContainer(
        isLoading: Boolean = true,
        content: @Composable () -> Unit = {}
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center)
                )
            }
        } else {
            content()
        }
    }

    private fun processImageProxy(
        scanner: BarcodeScanner,
        imageProxy: ImageProxy,
        onQrDetected: (String) -> Unit
    ) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    barcodes.firstOrNull()?.rawValue?.let { qr ->
                        onQrDetected(qr)
                    }
                }
                .addOnFailureListener { it.printStackTrace() }
                .addOnCompleteListener { imageProxy.close() }
        } else {
            imageProxy.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
