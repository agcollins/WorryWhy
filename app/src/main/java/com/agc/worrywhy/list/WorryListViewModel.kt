package com.agc.worrywhy.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agc.worrywhy.persistence.view.CompleteWorry
import com.agc.worrywhy.repository.WorryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorryListViewModel @Inject constructor(
    private val worryRepository: WorryRepository
) : ViewModel() {
    private val mutableFlow = MutableStateFlow<List<CompleteWorry>?>(null)
    val worries: StateFlow<List<CompleteWorry>?> = mutableFlow

    init {
        viewModelScope.launch(Dispatchers.IO) {
            mutableFlow.emitAll(worryRepository.getAllWorries())
        }
    }

    fun removeAllWorries() {
        viewModelScope.launch {
            worryRepository.deleteAll()
        }
    }
}

