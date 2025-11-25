package com.example.qrscan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import md.keeproblems.recieptparser.ui.qrscan.QrScanViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


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
            val state by viewModel.state.collectAsState()

            val context = LocalContext.current

            Box(modifier = Modifier.fillMaxSize()) {
                if (scannedQr == null) {
                    AndroidView(
                        factory = { ctx ->
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
                                                if (scannedQr == null) scannedQr = qr
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

                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                scannedQr?.let { qr ->
                    LoadingContainer(isLoading = state.isLoading) {
                        Text(text = "QR: $qr", modifier = Modifier.fillMaxSize())

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
                                        Text(it.productName, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                        Text(it.productPrice, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                    }
                                }
                            }
                        }
                    }
                    LaunchedEffect(qr) {
                        Toast.makeText(this@QrScanActivity, "QR: $qr", Toast.LENGTH_SHORT).show()
                        viewModel.updateProducts(url = qr)
                        setResult(RESULT_OK, intent.apply { putExtra("qr_result", qr) })
                    }
                    LaunchedEffect(state.errorMessage) {
                        Toast.makeText(this@QrScanActivity, state.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                        viewModel.clearError()
                    }
                }
            }
        }
    }

    @Composable
    private fun LoadingContainer(isLoading: Boolean = true, content: @Composable () -> Unit = {}) {
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

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(
        scanner: BarcodeScanner,
        imageProxy: ImageProxy,
        onQrDetected: (String) -> Unit
    ) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
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
