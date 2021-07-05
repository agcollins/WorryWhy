package com.agc.worrywhy.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agc.worrywhy.repository.MonthWorries
import com.agc.worrywhy.repository.WorryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: WorryRepository
) : ViewModel() {
    private val mutableStateFlow = MutableStateFlow<Map<LocalDate, Int>?>(null)
    val monthWorries: StateFlow<Map<LocalDate, Int>?> = mutableStateFlow
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWorriesForMonth(YearMonth.now()).map { worries ->
                val map = mutableMapOf<LocalDate, Int>()

                for (day in worries.worries) {
                    map[day.day] = day.count
                }

                map
            }.also {
                mutableStateFlow.emitAll(it)
            }
        }
    }
}