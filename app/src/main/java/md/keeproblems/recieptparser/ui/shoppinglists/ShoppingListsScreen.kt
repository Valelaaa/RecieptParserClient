package md.keeproblems.recieptparser.ui.shoppinglists

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.keeproblems.recieptparser.data.repository.UserReceiptRepository
import md.keeproblems.recieptparser.domain.models.ReceiptData
import md.keeproblems.recieptparser.ui.common.atomic.TextAtom
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.formatReceiptDate
import md.keeproblems.recieptparser.utils.formatTime
import md.keeproblems.recieptparser.utils.textResource
import javax.inject.Inject
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.res.painterResource
import md.keeproblems.recieptparser.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun ShoppingListsScreen(
    onBackClick: () -> Unit,
    onReceiptClick: (String) -> Unit,
    onDonatChartClick: () -> Unit,
    viewModel: ShoppingListViewModel = hiltViewModel<ShoppingListViewModel>()
) {
    val state by viewModel.state.collectAsState()
    Scaffold(topBar = {
        ShoppingListsTopBar(onBackClick = onBackClick, onDonatChartClick = onDonatChartClick)
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            ShoppingListsScreenContent(state.receipts, onReceiptClick = onReceiptClick)
        }
    }
}

@HiltViewModel
internal class ShoppingListViewModel @Inject constructor(
    val receiptRepository: UserReceiptRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ShoppingListViewState.empty)
    val state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ShoppingListViewState.empty
    )

    init {
        fetchShoppingList()
    }

    fun fetchShoppingList() {
        viewModelScope.launch {
            val products = receiptRepository.getAllProducts()
            _state.update { it.copy(receipts = products) }
        }
    }
}

internal data class ShoppingListViewState(
    val isLoading: Boolean = false,
    val receipts: List<ReceiptData> = emptyList(),
) {
    companion object {
        val empty = ShoppingListViewState()
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun ShoppingListsScreenContent(
    receipts: List<ReceiptData>,
    onReceiptClick: (String) -> Unit,
) {
    ShoppingListInterval(
        productList = receipts,
        onReceiptClick = onReceiptClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListsTopBar(onBackClick: () -> Unit, onDonatChartClick: () -> Unit) {
    CenterAlignedTopAppBar(
        modifier = Modifier.padding(horizontal = 16.dp),
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = MaterialTheme.colorScheme.background
        ),
        title = {
            TextAtom(
                style = AppTextStyle.TitleSmall,
                text = textResource("Shopping Lists"),
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        },
        actions = {
            IconButton({
                onDonatChartClick()
            }
            ) {
                Icon(painterResource(R.drawable.donut_chart), null, modifier = Modifier.size(24.dp))
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun ShoppingListInterval(
    productList: List<ReceiptData>,
    onReceiptClick: (String) -> Unit
) {
    val groupedByPeriod = productList.groupBy { it.receiptDate }
        .toSortedMap(compareByDescending { it })
    LazyColumn {
        groupedByPeriod.forEach { (period, receipts) ->

            item {
                TextAtom(
                    text = textResource(formatReceiptDate(period)),
                    style = AppTextStyle.LabelMediumSemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    softWrap = true,
                    maxLines = 2,
                )
            }

            items(receipts, key = { it.id }) { receipt ->
                ShoppingListItem(receipt, onClick = {
                    onReceiptClick(receipt.id)
                })
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun ShoppingListItem(receipt: ReceiptData, onClick: () -> Unit) {
    val receiptDate = formatReceiptDate(receipt.receiptDate)
    val receiptTime = formatTime(receipt.receiptTime)
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.padding(vertical = 12.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .weight(1f)
                ) {
                    TextAtom(
                        textResource(receipt.companyName),
                        style = AppTextStyle.LabelMediumSemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        softWrap = true,
                        maxLines = 2
                    )
                    TextAtom(
                        textResource("$receiptDate, $receiptTime"),
                        style = AppTextStyle.LabelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                TextAtom(
                    textResource(receipt.priceInfo.toString()),
                    style = AppTextStyle.LabelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            HorizontalDivider()
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                val itemsLabel = when (receipt.products.size) {
                    1 -> "item"
                    else -> "items"
                }

                val categories = receipt.products
                    .map { it.category.name }
                    .filter { it.isNotEmpty() }
                    .distinct()
                    .joinToString(" • ")

                TextAtom(
                    textResource("${receipt.products.size} $itemsLabel • $categories"),
                    style = AppTextStyle.LabelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ShoppingListsScreenPreview() {
    RecieptParserTheme {
        ShoppingListsScreen(onBackClick = {}, onDonatChartClick = {}, onReceiptClick = {})
    }
}