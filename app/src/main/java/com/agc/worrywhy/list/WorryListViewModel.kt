package com.agc.worrywhy.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agc.worrywhy.persistence.WorryDao
import com.agc.worrywhy.persistence.view.CompleteWorry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorryListViewModel @Inject constructor(
    private val worryDao: WorryDao
) : ViewModel() {
    private val mutableFlow = MutableStateFlow<List<CompleteWorry>?>(null)
    val worries: StateFlow<List<CompleteWorry>?> = mutableFlow

    init {
        viewModelScope.launch {
            mutableFlow.emitAll(worryDao.getAllComplete())
        }
    }

    fun removeAllWorries() {
        viewModelScope.launch {
            worryDao.deleteAll()
        }
    }
}

