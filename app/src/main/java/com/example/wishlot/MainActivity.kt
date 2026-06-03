package com.example.wishlot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wishlot.ui.screens.AddEditWishScreen
import com.example.wishlot.ui.screens.HistoryScreen
import com.example.wishlot.ui.screens.NoCandidatesScreen
import com.example.wishlot.ui.screens.SettingsScreen
import com.example.wishlot.ui.screens.SpinResultScreen
import com.example.wishlot.ui.screens.TreatYourselfScreen
import com.example.wishlot.ui.screens.WishlistScreen
import com.example.wishlot.ui.theme.WishlotTheme
import com.example.wishlot.viewmodel.PickUiState
import com.example.wishlot.viewmodel.WishlotViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WishlotTheme {
                WishlotApp()
            }
        }
    }
}

@Composable
fun WishlotApp(viewModel: WishlotViewModel = viewModel()) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.WISHLIST) }
    val snackbarHostState = remember { SnackbarHostState() }

    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val editDraft by viewModel.editDraft.collectAsState()
    val pickState by viewModel.pickState.collectAsState()
    val budgetInput by viewModel.budgetInput.collectAsState()
    val activeWishes by viewModel.activeWishes.collectAsState()
    val fulfilledWishes by viewModel.fulfilledWishes.collectAsState()
    val showHistory by viewModel.showHistory.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val showPickOverlay = pickState is PickUiState.Spinning || pickState is PickUiState.AwaitingDecision
    val showNoCandidates = pickState is PickUiState.NoCandidates

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearError()
        }
    }

    LaunchedEffect(successMessage) {
        successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSuccess()
        }
    }

    BackHandler(enabled = editDraft != null || showPickOverlay || showNoCandidates || showHistory) {
        when {
            editDraft != null -> viewModel.dismissEdit()
            showHistory -> viewModel.dismissHistory()
            showNoCandidates -> viewModel.dismissNoCandidates()
            showPickOverlay -> viewModel.dismissPick()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            NavigationSuiteScaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                navigationSuiteItems = {
                    AppDestinations.entries.forEach { destination ->
                        item(
                            icon = {
                                if (destination == AppDestinations.TREAT) {
                                    Image(
                                        painter = painterResource(R.drawable.wheel_disc),
                                        contentDescription = destination.label,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape),
                                    )
                                } else {
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = destination.label,
                                    )
                                }
                            },
                            label = { Text(destination.label) },
                            selected = destination == currentDestination,
                            onClick = { currentDestination = destination },
                        )
                    }
                },
            ) {
                when (currentDestination) {
                    AppDestinations.WISHLIST -> WishlistScreen(
                        wishes = activeWishes,
                        formatPrice = viewModel::formatPrice,
                        onWishClick = viewModel::openEditWish,
                        onDeleteSwipe = viewModel::deleteWish,
                        onAddClick = viewModel::openAddWish,
                    )
                    AppDestinations.TREAT -> TreatYourselfScreen(
                        budgetInput = budgetInput,
                        onBudgetChange = viewModel::setBudgetInput,
                        hasActiveWishes = activeWishes.isNotEmpty(),
                        onSpin = viewModel::startPick,
                    )
                    AppDestinations.SETTINGS -> SettingsScreen(
                        stats = stats,
                        formatPrice = viewModel::formatPrice,
                        onOpenHistory = viewModel::openHistory,
                        versionName = viewModel.versionName,
                        buildDate = viewModel.buildDate,
                    )
                }
            }
        }

        editDraft?.let { draft ->
            AddEditWishScreen(
                draft = draft,
                onBack = viewModel::dismissEdit,
                onSave = viewModel::saveWish,
                onDelete = viewModel::deleteWish,
            )
        }

        if (showHistory) {
            HistoryScreen(
                wishes = fulfilledWishes,
                formatPrice = viewModel::formatPrice,
                onRestore = viewModel::restoreWish,
                onBack = viewModel::dismissHistory,
            )
        }

        if (showNoCandidates) {
            NoCandidatesScreen(
                onChangeBudget = viewModel::dismissNoCandidates,
                onGoWishlist = {
                    viewModel.dismissNoCandidates()
                    currentDestination = AppDestinations.WISHLIST
                },
            )
        }

        if (showPickOverlay) {
            SpinResultScreen(
                pickState = pickState,
                formatPrice = viewModel::formatPrice,
                onSpinAnimationComplete = viewModel::onSpinAnimationComplete,
                onAccept = viewModel::acceptPick,
                onBack = viewModel::dismissPick,
            )
        }
    }
}

