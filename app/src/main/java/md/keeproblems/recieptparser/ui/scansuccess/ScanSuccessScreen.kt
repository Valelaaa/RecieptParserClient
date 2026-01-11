package md.keeproblems.recieptparser.ui.scansuccess

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import md.keeproblems.recieptparser.domain.models.PriceInfo
import md.keeproblems.recieptparser.ui.common.atomic.TextAtom
import md.keeproblems.recieptparser.ui.common.buttons.PrimaryButton
import md.keeproblems.recieptparser.ui.common.buttons.SecondaryButton
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.textResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SuccessScanResult(
    onBackClick: () -> Unit,
    priceInfo: PriceInfo = PriceInfo(value = "0"),
    date: String,
    onSaveReceipt: () -> Unit = {},
    onReceiptDetails: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { contentPaddings ->
        Column(
            modifier = Modifier
                .padding(contentPaddings)
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            SuccessScanResultContent(
                priceInfo = priceInfo,
                date = date,
                onSaveReceipt = onSaveReceipt,
                onReceiptDetails = onReceiptDetails
            )
        }
    }
}

@Composable
internal fun SuccessScanResultContent(
    priceInfo: PriceInfo = PriceInfo(value = "0"),
    date: String,
    onSaveReceipt: () -> Unit,
    onReceiptDetails: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextAtom(
                textResource("Scan Receipt"),
                style = AppTextStyle.DisplayMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )
            Box(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
                    .padding(24.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(72.dp)
                )
            }
            TextAtom(
                text = textResource("Successfully scanned!"),
                style = AppTextStyle.Custom(
                    custom =
                        AppTextStyle.DisplaySmall
                            .textStyle.copy(fontWeight = FontWeight.Medium)
                ),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextAtom(
                text = textResource("$priceInfo • $date"),
                style = AppTextStyle.TitleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PrimaryButton(
                onClick = onSaveReceipt,
                text = textResource("Save"),
                modifier = Modifier.fillMaxWidth()
            )
            SecondaryButton(
                onClick = onReceiptDetails,
                text = textResource("Details"),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun SuccessScanResultPreview() {
    val currentDate = remember {
        val formatter = DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.ENGLISH)
        LocalDate.now().format(formatter)
    }
    RecieptParserTheme {
        SuccessScanResult(onBackClick = {}, date = currentDate)
    }
}