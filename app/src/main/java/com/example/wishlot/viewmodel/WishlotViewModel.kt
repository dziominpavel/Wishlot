package com.example.wishlot.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wishlot.BuildConfig
import com.example.wishlot.R
import com.example.wishlot.data.MoneyUtils
import com.example.wishlot.data.SettingsRepository
import com.example.wishlot.data.Wish
import com.example.wishlot.data.WishRepository
import com.example.wishlot.data.WishStatus
import com.example.wishlot.data.backup.BackupRepository
import com.example.wishlot.data.pick.PickInput
import com.example.wishlot.data.pick.PickResult
import com.example.wishlot.data.pick.WishPicker
import com.example.wishlot.data.pick.toSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class WishlotViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WishRepository.getInstance(application)
    private val settings = SettingsRepository.getInstance(application)
    private val backupRepository = BackupRepository.getInstance(application)

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _editDraft = MutableStateFlow<WishEditDraft?>(null)
    val editDraft: StateFlow<WishEditDraft?> = _editDraft.asStateFlow()

    private val _pickState = MutableStateFlow<PickUiState>(PickUiState.Idle)
    val pickState: StateFlow<PickUiState> = _pickState.asStateFlow()

    private val _budgetInput = MutableStateFlow("")
    val budgetInput: StateFlow<String> = _budgetInput.asStateFlow()

    private val _showArchived = MutableStateFlow(false)
    val showArchived: StateFlow<Boolean> = _showArchived.asStateFlow()

    private val _showHistory = MutableStateFlow(false)
    val showHistory: StateFlow<Boolean> = _showHistory.asStateFlow()

    val activeWishes = repository.observeActive()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val fulfilledWishes = repository.observeFulfilled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val archivedWishes = repository.observeArchived()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val stats = combine(activeWishes, fulfilledWishes, archivedWishes) { active, fulfilled, archived ->
        WishStats(
            activeCount = active.size,
            activeTotalMinor = active.sumOf { it.priceMinor },
            fulfilledCount = fulfilled.size,
            fulfilledTotalMinor = fulfilled.sumOf { it.priceMinor },
            archivedCount = archived.size,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WishStats())

    val versionName: String = BuildConfig.VERSION_NAME
    val buildDate: String = BuildConfig.BUILD_DATE

    init {
        viewModelScope.launch {
            settings.lastTreatBudgetMinor.collect { saved ->
                if (saved != null && saved > 0 && _budgetInput.value.isBlank()) {
                    _budgetInput.value = MoneyUtils.minorToDisplayInput(saved)
                }
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccess() {
        _successMessage.value = null
    }

    fun setBudgetInput(value: String) {
        _budgetInput.value = value
    }

    fun openAddWish() {
        _editDraft.value = WishEditDraft()
    }

    fun openEditWish(id: Long) {
        safeDb(getString(R.string.error_load_failed)) {
            val wish = repository.getById(id) ?: return@safeDb
            _editDraft.value = wish.toDraft()
        }
    }

    fun dismissEdit() {
        _editDraft.value = null
    }

    fun openArchived() {
        _showArchived.value = true
    }

    fun dismissArchived() {
        _showArchived.value = false
    }

    fun openHistory() {
        _showHistory.value = true
    }

    fun dismissHistory() {
        _showHistory.value = false
    }

    fun saveWish(draft: WishEditDraft) {
        val title = draft.title.trim()
        if (title.isEmpty()) {
            _errorMessage.value = getString(R.string.error_empty_title)
            return
        }
        val priceMinor = MoneyUtils.parseRublesInput(draft.priceInput)
        if (priceMinor == null) {
            _errorMessage.value = getString(R.string.error_invalid_price)
            return
        }

        safeDb(getString(R.string.error_save_failed)) {
            val note = draft.note.trim().ifEmpty { null }
            if (draft.id != null) {
                val existing = repository.getById(draft.id) ?: return@safeDb
                repository.update(
                    existing.copy(
                        title = title,
                        priceMinor = priceMinor,
                        note = note,
                        category = draft.category.name,
                    ),
                )
            } else {
                val now = System.currentTimeMillis()
                val sortOrder = repository.nextSortOrder()
                repository.insert(
                    Wish(
                        clientId = UUID.randomUUID().toString(),
                        title = title,
                        priceMinor = priceMinor,
                        note = note,
                        status = WishStatus.ACTIVE.name,
                        category = draft.category.name,
                        sortOrder = sortOrder,
                        createdAt = now,
                        fulfilledAt = null,
                    ),
                )
            }
            _editDraft.value = null
        }
    }

    fun deleteWish(id: Long) {
        safeDb(getString(R.string.error_delete_failed)) {
            repository.delete(id)
            if (_editDraft.value?.id == id) {
                _editDraft.value = null
            }
        }
    }

    fun archiveWish(id: Long) {
        safeDb(getString(R.string.error_save_failed)) {
            repository.markArchived(id)
            if (_editDraft.value?.id == id) {
                _editDraft.value = null
            }
        }
    }

    fun restoreWish(id: Long) {
        safeDb(getString(R.string.error_save_failed)) {
            repository.restoreToActive(id)
            _successMessage.value = getString(R.string.restore_success)
        }
    }

    fun startPick() {
        val budgetMinor = MoneyUtils.parseRublesInput(_budgetInput.value)
        if (budgetMinor == null) {
            _errorMessage.value = getString(R.string.error_invalid_budget)
            return
        }
        if (activeWishes.value.isEmpty()) {
            _errorMessage.value = getString(R.string.error_no_active_wishes)
            return
        }

        safeDb(getString(R.string.error_pick_failed)) {
            settings.setLastTreatBudgetMinor(budgetMinor)
            val snapshots = activeWishes.value.map { it.toSnapshot() }
            when (val result = WishPicker.runPick(PickInput(budgetMinor, snapshots), Random.Default)) {
                PickResult.NoCandidates -> _pickState.value = PickUiState.NoCandidates
                is PickResult.Winner -> {
                    val winner = activeWishes.value.first { it.id == result.wish.id }
                    val wheelItems = buildWheelItems(activeWishes.value, winner)
                    _pickState.value = PickUiState.Spinning(winner, wheelItems)
                }
            }
        }
    }

    fun onSpinAnimationComplete() {
        val state = _pickState.value
        if (state is PickUiState.Spinning) {
            _pickState.value = PickUiState.AwaitingDecision(state.winner)
        }
    }

    fun acceptPick() {
        val winner = (_pickState.value as? PickUiState.AwaitingDecision)?.winner ?: return
        safeDb(getString(R.string.error_save_failed)) {
            repository.markFulfilled(winner.id)
            dismissPick()
        }
    }

    fun declinePick() {
        dismissPick()
    }

    fun spinAgain() {
        dismissPick()
        startPick()
    }

    fun dismissPick() {
        _pickState.value = PickUiState.Idle
    }

    fun dismissNoCandidates() {
        _pickState.value = PickUiState.Idle
    }

    suspend fun exportBackupJson(): String = backupRepository.exportJson()

    fun importBackupJson(json: String) {
        safeDb(getString(R.string.error_import_failed)) {
            val count = backupRepository.importJson(json)
            _successMessage.value = getApplication<Application>().getString(R.string.import_success, count)
        }
    }

    fun formatPrice(minor: Long): String = MoneyUtils.formatMinor(minor)

    private fun Wish.toDraft() = WishEditDraft(
        id = id,
        title = title,
        priceInput = MoneyUtils.minorToDisplayInput(priceMinor),
        note = note.orEmpty(),
        category = com.example.wishlot.data.WishCategory.fromName(category),
    )

    private fun buildWheelItems(active: List<Wish>, winner: Wish): List<Wish> {
        if (active.size <= 8) return active
        val others = active.filter { it.id != winner.id }.shuffled().take(7)
        return (others + winner).shuffled()
    }

    private fun getString(resId: Int): String = getApplication<Application>().getString(resId)

    private fun safeDb(errorFallback: String, block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: errorFallback
                Log.e(TAG, errorFallback, e)
            }
        }
    }

    companion object {
        private const val TAG = "WishlotViewModel"
    }
}
