package md.keeproblems.recieptparser

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import md.keeproblems.recieptparser.ui.qrscan.QrScanActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.keeproblems.recieptparser.data.repository.UserReceiptRepository
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.navArgument
import md.keeproblems.recieptparser.domain.models.ReceiptData
import md.keeproblems.recieptparser.ui.dashboard.DashboardScreen
import md.keeproblems.recieptparser.ui.scanneddetails.ReceiptDetails
import md.keeproblems.recieptparser.ui.scanneddetails.ReceiptDetailsWrapper
import md.keeproblems.recieptparser.ui.shoppinglists.ShoppingListsScreen
import md.keeproblems.recieptparser.ui.theme.RecieptParserTheme
import javax.inject.Inject

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
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
            val navController = rememberNavController()
            RecieptParserTheme {
                NavHost(navController, startDestination = MainNavigation.DashboardScreen.route) {
                    composable(MainNavigation.DashboardScreen.route) {
                        DashboardScreen(
                            onScanClick = ::navigateToScanActivity,
                            scanResultFlow = scanResultFlow,
                            onReceiptClick = { id ->
                                navController.navigate(
                                    MainNavigation.ReceiptDescription.createRoute(
                                        id
                                    )
                                )
                            },
                            onViewAllClick = { navController.navigate(MainNavigation.ShoppingLists.route) }
                        )
                    }
                    composable(MainNavigation.ShoppingLists.route) {
                        ShoppingListsScreen(
                            onBackClick = {
                                navController.popBackStack()
                            },
                            onReceiptClick = { id ->
                                navController.navigate(
                                    MainNavigation.ReceiptDescription.createRoute(
                                        id
                                    )
                                )
                            })
                    }
                    composable(
                        MainNavigation.ReceiptDescription.routeWithArgs, arguments = listOf(
                            navArgument(MainNavigation.ReceiptDescription.argName) {
                                type = NavType.StringType
                            }
                        )) { backStackEntry ->
                        val receiptId =
                            backStackEntry.arguments?.getString(MainNavigation.ReceiptDescription.argName)
                        RecieptDetailsScreen(
                            receiptId ?: "",
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }

    private fun navigateToScanActivity() {
        val intent = Intent(this, QrScanActivity::class.java)
        scanLauncher.launch(intent)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
internal fun RecieptDetailsScreen(
    receiptId: String,
    onBackClick: () -> Unit,
    viewModel: ReceiptDetailsViewModel = hiltViewModel<ReceiptDetailsViewModel>()
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.initData(receiptId)
    }
    ReceiptDetailsWrapper(
        receiptData = state.receipt,
        onBackClick = onBackClick,
        moreOptionsClick = {},
        onSaveClick = {},
        onShareClick = {},
        shouldShowButtons = false
    )
}

@HiltViewModel
internal class ReceiptDetailsViewModel @Inject constructor(
    private val userReceiptRepository: UserReceiptRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ReceiptDetailsViewState>(ReceiptDetailsViewState.empty)
    val state = _state.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000),
        ReceiptDetailsViewState.empty
    )

    fun initData(receiptId: String) {
        viewModelScope.launch {
            val receipt = userReceiptRepository.getProductsById(receiptId)
            _state.update {
                it.copy(receipt = receipt)
            }
        }
    }
}

internal data class ReceiptDetailsViewState(
    val isLoading: Boolean = false,
    val receipt: ReceiptData? = null
) {
    companion object {
        val empty = ReceiptDetailsViewState()
    }
}


sealed class MainNavigation(val route: String) {
    data object DashboardScreen : MainNavigation("dashboard_screen")
    data object ShoppingLists : MainNavigation("shopping_lists")
    data object ReceiptDescription : MainNavigation("list_description") {
        const val argName = "receiptId"
        val routeWithArgs = "$route/{$argName}"

        fun createRoute(id: String) = "$route/$id"
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