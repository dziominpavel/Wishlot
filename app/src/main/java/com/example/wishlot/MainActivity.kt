package com.example.wishlot

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wishlot.ui.screens.AddEditWishScreen
import com.example.wishlot.ui.screens.ArchivedScreen
import com.example.wishlot.ui.screens.HistoryScreen
import com.example.wishlot.ui.screens.NoCandidatesScreen
import com.example.wishlot.ui.screens.SettingsScreen
import com.example.wishlot.ui.screens.SpinResultScreen
import com.example.wishlot.ui.screens.TreatYourselfScreen
import com.example.wishlot.ui.screens.WishlistScreen
import com.example.wishlot.ui.theme.WishlotTheme
import com.example.wishlot.viewmodel.PickUiState
import com.example.wishlot.viewmodel.WishlotViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val errorMessage by viewModel.errorMessage.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()
    val editDraft by viewModel.editDraft.collectAsState()
    val pickState by viewModel.pickState.collectAsState()
    val budgetInput by viewModel.budgetInput.collectAsState()
    val activeWishes by viewModel.activeWishes.collectAsState()
    val fulfilledWishes by viewModel.fulfilledWishes.collectAsState()
    val archivedWishes by viewModel.archivedWishes.collectAsState()
    val showArchived by viewModel.showArchived.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val showPickOverlay = pickState is PickUiState.Spinning || pickState is PickUiState.AwaitingDecision
    val showNoCandidates = pickState is PickUiState.NoCandidates

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        scope.launch {
            try {
                val json = viewModel.exportBackupJson()
                writeUri(context, uri, json)
                snackbarHostState.showSnackbar(context.getString(R.string.export_success))
            } catch (e: Exception) {
                snackbarHostState.showSnackbar(e.message ?: context.getString(R.string.error_export_failed))
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        scope.launch {
            try {
                val json = readUri(context, uri)
                viewModel.importBackupJson(json)
            } catch (e: Exception) {
                snackbarHostState.showSnackbar(e.message ?: "Import failed")
            }
        }
    }

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

    BackHandler(enabled = editDraft != null || showPickOverlay || showNoCandidates || showArchived) {
        when {
            editDraft != null -> viewModel.dismissEdit()
            showArchived -> viewModel.dismissArchived()
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
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.label,
                                )
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
                        onAddClick = viewModel::openAddWish,
                    )
                    AppDestinations.TREAT -> TreatYourselfScreen(
                        budgetInput = budgetInput,
                        onBudgetChange = viewModel::setBudgetInput,
                        hasActiveWishes = activeWishes.isNotEmpty(),
                        onSpin = viewModel::startPick,
                    )
                    AppDestinations.HISTORY -> HistoryScreen(
                        wishes = fulfilledWishes,
                        formatPrice = viewModel::formatPrice,
                        onRestore = viewModel::restoreWish,
                    )
                    AppDestinations.SETTINGS -> SettingsScreen(
                        stats = stats,
                        formatPrice = viewModel::formatPrice,
                        onOpenArchived = viewModel::openArchived,
                        onExportBackup = {
                            val stamp = SimpleDateFormat("yyyyMMdd_HHmm", Locale.US).format(Date())
                            exportLauncher.launch("wishlot_backup_$stamp.json")
                        },
                        onImportBackup = {
                            importLauncher.launch(arrayOf("application/json", "text/plain", "*/*"))
                        },
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
                onArchive = if (draft.id != null) viewModel::archiveWish else null,
            )
        }

        if (showArchived) {
            ArchivedScreen(
                wishes = archivedWishes,
                formatPrice = viewModel::formatPrice,
                onBack = viewModel::dismissArchived,
                onRestore = viewModel::restoreWish,
                onDelete = viewModel::deleteWish,
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
                onDecline = viewModel::declinePick,
                onSpinAgain = viewModel::spinAgain,
                onBack = viewModel::dismissPick,
            )
        }
    }
}

private suspend fun readUri(context: Context, uri: Uri): String = withContext(Dispatchers.IO) {
    context.contentResolver.openInputStream(uri)?.use { stream ->
        stream.bufferedReader().readText()
    } ?: error("Cannot read file")
}

private suspend fun writeUri(context: Context, uri: Uri, content: String) = withContext(Dispatchers.IO) {
    context.contentResolver.openOutputStream(uri)?.use { stream ->
        stream.write(content.toByteArray(Charsets.UTF_8))
    } ?: error("Cannot write file")
}
