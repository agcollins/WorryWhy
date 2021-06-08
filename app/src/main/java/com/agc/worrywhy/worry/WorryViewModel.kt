package com.agc.worrywhy.worry

import androidx.lifecycle.*
import com.agc.worrywhy.persistence.WorryDao
import com.agc.worrywhy.persistence.entity.Worry
import com.agc.worrywhy.persistence.relationship.WorryWithInstancesAndText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorryViewModel @Inject constructor(
    private val worryDao: WorryDao
) : ViewModel() {
    private val worryFlow = MutableStateFlow<WorryWithInstancesAndText?>(null)
    val worry: StateFlow<WorryWithInstancesAndText?> = worryFlow

    fun select(worryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            worryFlow.emitAll(worryDao.getWorryWithInstancesAndText(worryId))
        }
    }

    fun deleteWorry(worryId: Long) {
        viewModelScope.launch {
            worryDao.deleteWorry(worryId)
        }
    }

    fun deleteOccurrence(occurrenceId: Long) {
        viewModelScope.launch {
            worryDao.deleteWorryInstance(occurrenceId)
        }
    }
}