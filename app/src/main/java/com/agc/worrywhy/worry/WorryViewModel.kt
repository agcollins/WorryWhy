package com.agc.worrywhy.worry

import androidx.lifecycle.*
import com.agc.worrywhy.persistence.Worry
import com.agc.worrywhy.persistence.WorryDao
import com.agc.worrywhy.persistence.WorryInstance
import com.agc.worrywhy.persistence.WorryWithInstances
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorryViewModel @Inject constructor(
    private val worryDao: WorryDao
) : ViewModel() {
    private val worryFlow = MutableStateFlow<WorryWithInstances?>(null)
    val worry = worryFlow.asLiveData()

    fun select(worryId: Long) {
        viewModelScope.launch {
            worryFlow.emitAll(worryDao.getWorryWithInstances(worryId))
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