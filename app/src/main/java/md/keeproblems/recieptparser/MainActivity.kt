package md.keeproblems.recieptparser

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import md.keeproblems.recieptparser.ui.qrscan.QrScanActivity
import dagger.hilt.android.AndroidEntryPoint
import md.keeproblems.recieptparser.ui.dashboard.DashboardScreen
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecieptParserTheme {
                DashboardScreen(
                    onScanClick = ::navigateToScanActivity
                )
            }
        }
    }

    private fun navigateToScanActivity() {
        val intent = Intent(this, QrScanActivity::class.java)
        startActivity(intent)
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