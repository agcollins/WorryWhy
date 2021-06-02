package com.agc.worrywhy.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agc.worrywhy.persistence.entity.Worry
import com.agc.worrywhy.persistence.WorryDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWorryViewModel @Inject constructor(
    private val worryDao: WorryDao
) : ViewModel() {
    suspend fun addWorry(text: String): Long {
        return worryDao.addWorry(Worry(text))
    }

    fun addWorryOccurrence(worryId: Long, whatHappened: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            worryDao.addWorryInstance(worryId, whatHappened)
        }
    }
}