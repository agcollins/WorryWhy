package com.agc.worrywhy.day

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agc.worrywhy.persistence.relationship.WorryWithInstancesAndText
import com.agc.worrywhy.repository.WorryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WorryDayViewModel @Inject constructor(
    private val repository: WorryRepository
) : ViewModel() {
    private val mutableWorryFlow = MutableStateFlow<List<WorryWithInstancesAndText>>(emptyList())
    val worryFlow: StateFlow<List<WorryWithInstancesAndText>> = mutableWorryFlow

    fun select(day: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableWorryFlow.emitAll(
                repository.getDayWorries(day)
            )
        }
    }
}