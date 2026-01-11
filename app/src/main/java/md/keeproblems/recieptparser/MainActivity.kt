package md.keeproblems.recieptparser

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import md.keeproblems.recieptparser.ui.qrscan.QrScanActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import md.keeproblems.recieptparser.ui.dashboard.DashboardScreen
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val scanResultFlow = MutableSharedFlow<Unit>()
    private val scanLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                scanResultFlow.emit(Unit)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecieptParserTheme {
                DashboardScreen(
                    onScanClick = ::navigateToScanActivity,
                    scanResultFlow = scanResultFlow
                )
            }
        }
    }

    private fun navigateToScanActivity() {
        val intent = Intent(this, QrScanActivity::class.java)
        scanLauncher.launch(intent)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecieptParserTheme {
        Greeting("Android")
    }
}