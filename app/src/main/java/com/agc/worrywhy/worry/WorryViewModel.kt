package com.agc.worrywhy.worry

import androidx.lifecycle.*
import com.agc.worrywhy.persistence.WorryDao
import com.agc.worrywhy.persistence.relationship.WorryWithInstancesAndText
import com.agc.worrywhy.repository.WorryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorryViewModel @Inject constructor(
    private val worryRepository: WorryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val worryFlow = MutableStateFlow<WorryWithInstancesAndText?>(null)
    val worry: StateFlow<WorryWithInstancesAndText?> = worryFlow

    init {
        savedStateHandle.get<Long>("worryId")?.let(this::select)
    }

    fun select(worryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            worryFlow.emitAll(worryRepository.getWorry(worryId))
        }
    }

    fun deleteWorry(worryId: Long) {
        viewModelScope.launch {
            worryRepository.deleteWorry(worryId)
        }
    }

    fun deleteOccurrence(occurrenceId: Long) {
        viewModelScope.launch {
            worryRepository.deleteWorryInstance(occurrenceId)
        }
    }

    fun saveTitle(newTitle: String, worryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            worryRepository.updateTitle(newTitle, worryId)
        }
    }
}