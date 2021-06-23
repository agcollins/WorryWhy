package com.agc.worrywhy.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agc.worrywhy.persistence.entity.Worry
import com.agc.worrywhy.persistence.WorryDao
import com.agc.worrywhy.repository.WorryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWorryViewModel @Inject constructor(
    private val worryRepository: WorryRepository
) : ViewModel() {
    suspend fun addWorry(text: String) = worryRepository.addWorry(text)

    fun addWorryOccurrence(worryId: Long, whatHappened: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            worryRepository.addWorryInstance(worryId, whatHappened)
        }
    }
}