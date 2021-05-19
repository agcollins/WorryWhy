package com.agc.worrywhy.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agc.worrywhy.WorryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorryListViewModel @Inject constructor(
    private val worryDao: WorryDao
) : ViewModel() {
    val worries = worryDao.getAll().flowOn(Dispatchers.Default).asLiveData()

    fun removeAllWorries() {
        viewModelScope.launch {
            worryDao.deleteAll()
        }
    }
}

