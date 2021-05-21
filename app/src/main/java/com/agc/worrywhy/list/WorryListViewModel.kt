package com.agc.worrywhy.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.agc.worrywhy.WorryDao
import com.agc.worrywhy.WorryInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorryListViewModel @Inject constructor(
    private val worryDao: WorryDao
) : ViewModel() {
    val worries = worryDao.getAllComplete().flowOn(Dispatchers.IO).asLiveData()

    fun removeAllWorries() {
        viewModelScope.launch(Dispatchers.IO) {
            worryDao.deleteAll()
        }
    }

    fun recordWorry(worryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            worryDao.addWorryInstance(WorryInstance(worryId))
        }
    }

    fun deleteWorry(worryId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            worryDao.deleteWorry(worryId)
        }
    }

    fun unRecordWorry() {
        viewModelScope.launch(Dispatchers.IO) {
            worryDao.removeLatestWorryInstance()
        }
    }
}

