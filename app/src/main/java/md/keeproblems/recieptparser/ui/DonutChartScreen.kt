package md.keeproblems.recieptparser.ui

import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.keeproblems.recieptparser.data.repository.UserReceiptRepository
import md.keeproblems.recieptparser.domain.models.Currency
import md.keeproblems.recieptparser.domain.models.PriceInfo
import md.keeproblems.recieptparser.domain.models.Product
import md.keeproblems.recieptparser.ui.common.atomic.TextAtom
import md.keeproblems.recieptparser.ui.theme.AppTextStyle
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import md.keeproblems.recieptparser.utils.textResource
import javax.inject.Inject
import kotlin.random.Random
import androidx.core.graphics.toColorInt
import kotlin.math.roundToInt

@HiltViewModel
internal class DonutChartViewModel @Inject constructor(private val userReceiptRepository: UserReceiptRepository) :
    ViewModel() {
    private val _state = MutableStateFlow<DonutChartViewState>(DonutChartViewState.empty)
    val state = _state.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000L), DonutChartViewState.empty
    )

    init {
        fetchAllProducts()
    }

    private fun fetchAllProducts() {
        viewModelScope.launch {
            val receipts = userReceiptRepository.getAllProducts()
            val mappedProducts: List<Product> = receipts.flatMap { it.products }
                .filterNot { it.productPrice.value.toDouble() == 0.0 }
                .filterNot { it.productName.startsWith("INTRODUS") }

            val grouped = mappedProducts.groupBy { it.category.name }
                .map { (name, products) ->
                    val totalValue = products.sumOf { it.productPrice.value.toDouble() }
                    name to totalValue
                }

            val categoryData = grouped
                .filter { (name, _) ->
                    mappedProducts.any { it.category.name == name && !it.category.colorValue.isNullOrBlank() }
                }
                .map { (name, totalValue) ->
                    val colorValue = mappedProducts
                        .first { it.category.name == name }
                        .category.colorValue

                    val color = Color(colorValue.removePrefix("0x").toLong(16).toInt())

                    CategoryChartView(
                        value = PriceInfo(
                            value = totalValue.toString(),
                            currency = state.value.currency
                        ),
                        name = name,
                        color = color
                    )
                }

            _state.update { state ->
                state.copy(allProducts = categoryData, filteredProducts = categoryData)
            }
        }
    }

    fun filterProductsByCategory(categoryName: String) {
        val filtered = state.value.allProducts.filter { it.name == categoryName }
        _state.update { it.copy(filteredProducts = filtered) }
    }

    fun clearFilter() {
        _state.update { it.copy(filteredProducts = state.value.allProducts) }
    }
}

internal data class DonutChartViewState(
    val allProducts: List<CategoryChartView> = emptyList(),
    val filteredProducts: List<CategoryChartView> = emptyList(),
    val currency: Currency = Currency.MDL,
) {
    companion object {
        val empty = DonutChartViewState()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DonutChartTopBar(
    onBackClick: () -> Unit,
) {
    TopAppBar(
        title = {
            TextAtom(
                textResource("My Expenses"),
                style = AppTextStyle.DisplayMedium,
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors()
            .copy(containerColor = MaterialTheme.colorScheme.background),
    )
}

@Composable
internal fun DonutChartScreen(
    onBackClick: () -> Unit,
    viewModel: DonutChartViewModel = hiltViewModel<DonutChartViewModel>()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(topBar = {
        DonutChartTopBar(onBackClick = onBackClick)
    }) { paddingValues ->
        DonutChartContent(
            state = state,
            onCategoryClick = viewModel::filterProductsByCategory,
            onCategoryCleared = viewModel::clearFilter,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        )
    }

}

@Composable
private fun TotalAmountComponent(totalAmount: PriceInfo) {
    val borderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
    val backgroundColor = MaterialTheme.colorScheme.surface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val gradient = Brush.horizontalGradient(
                    colors = listOf(
                        backgroundColor,
                        borderColor,
                        backgroundColor
                    ),
                    startX = 0f,
                    endX = size.width
                )
                drawLine(
                    brush = gradient,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 4f
                )
                drawLine(
                    brush = gradient,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 4f
                )
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextAtom(
            style = AppTextStyle.TitleMedium,
            text = textResource(totalAmount.toString()),
            color = MaterialTheme.colorScheme.primary,
            maxLines = Int.MAX_VALUE
        )
    }
}

@Composable
private fun DonutChartContent(
    state: DonutChartViewState,
    onCategoryClick: (String) -> Unit,
    onCategoryCleared: () -> Unit,
    modifier: Modifier = Modifier
) {
    val total = state.filteredProducts.sumOf { it.value.value.toDoubleOrNull() ?: 0.0 }
    val rounded = (total * 100).roundToInt() / 100.0
    DonutChart(
        modifier = modifier,
        categoryData = state.allProducts,
        filteredData = state.filteredProducts,
        totalFilteredAmount = PriceInfo(
            value = "$rounded",
            currency = Currency.MDL
        ),
        onCategoryClick = onCategoryClick,
        onCategoryCleared = onCategoryCleared,
        name = "January"
    )
}

internal data class CategoryChartView(
    val name: String, val value: PriceInfo, val color: Color
)

@Composable
private fun ChartLegend(
    data: List<CategoryChartView>, totalValue: Double, modifier: Modifier = Modifier
) {
    Column(modifier.padding(16.dp)) {
        data.forEach {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(color = it.color)
                    )
                    val percentage = (it.value.value.toDouble() / totalValue) * 100
                    val rounded = (percentage * 100).roundToInt() / 100.0
                    val formatted = "$rounded%"
                    TextAtom(
                        style = AppTextStyle.BodyLarge,
                        text = textResource("${it.name} $formatted"),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = Int.MAX_VALUE
                    )
                }
                val rounded = (it.value.value.toDouble() * 100).roundToInt() / 100.0

                TextAtom(
                    style = AppTextStyle.TitleSmall,
                    text = textResource(it.value.copy(rounded.toString()).toString()),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DonutChart(
    name: String,
    categoryData: List<CategoryChartView>,
    filteredData: List<CategoryChartView>,
    onCategoryClick: (String) -> Unit,
    totalFilteredAmount: PriceInfo,
    onCategoryCleared: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(16.dp)
                    .padding(top = 24.dp)
                    .align(Alignment.Center), factory = { context ->
                    PieChart(context).apply {
                        description.isEnabled = false
                        isDrawHoleEnabled = true
                        holeRadius = 60f
                        transparentCircleRadius = 65f
                        centerText = "January"
                        setUsePercentValues(true)
                        setDrawEntryLabels(false)
                        centerTextRadiusPercent = 50f
                        setCenterTextSize(20f)
                        legend.isEnabled = false
                        setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                            override fun onValueSelected(e: Entry?, h: Highlight?) {
                                val label = (e as? PieEntry)?.label ?: return
                                onCategoryClick(label)
                            }

                            override fun onNothingSelected() {
                                onCategoryCleared()
                            }
                        })
                    }
                }, update = { chart ->
                    val entries = categoryData.map { value ->
                        PieEntry(value.value.value.toFloat(), value.name)
                    }

                    val dataSet = PieDataSet(entries, "").apply {
                        this.colors = categoryData.map { it.color.toArgb() }
                        sliceSpace = 2f
                        valueTextSize = 14f
                        valueTextColor = android.graphics.Color.WHITE

                        setDrawValues(false)
                    }

                    val data = PieData(dataSet)

                    chart.data = data
                    chart.invalidate()
                }
            )
        }
        TotalAmountComponent(totalAmount = totalFilteredAmount)
        ChartLegend(data = filteredData, totalValue = filteredData.fold(0.0) { sum, item ->
            sum + item.value.value.toDouble()
        })
    }
}

@Preview
@Composable
private fun DonutChartScreenPreview() {
    RecieptParserTheme {
        DonutChartScreen(onBackClick = {})
    }
}