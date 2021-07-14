package com.agc.worrywhy.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agc.worrywhy.repository.WorryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: WorryRepository
) : ViewModel() {
    private val monthHeaderMutable = MutableStateFlow<Map<YearMonth, MonthSummary>?>(null)
    val monthHeader: StateFlow<Map<YearMonth, MonthSummary>?> = monthHeaderMutable

    private val dayStateFlow = MutableStateFlow<Map<LocalDate, Int>?>(null)
    val monthWorries: StateFlow<Map<LocalDate, Int>?> = dayStateFlow

    init {
        select()
    }

    data class MonthSummary(val month: YearMonth, val count: Int)

    private fun select() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllMonthWorries().map { months ->
                val map = mutableMapOf<LocalDate, Int>()
                val monthMap = mutableMapOf<YearMonth, MonthSummary>()

                for (month in months) {
                    var countInMonth = 0
                    for (worries in month.worries) {
                        map[worries.day] = worries.count
                        countInMonth += worries.count
                    }

                    monthMap[month.yearMonth] = MonthSummary(month.yearMonth, countInMonth)
                }

                monthHeaderMutable.value = monthMap
                map
            }.also {
                dayStateFlow.emitAll(it)
            }
        }
    }
}