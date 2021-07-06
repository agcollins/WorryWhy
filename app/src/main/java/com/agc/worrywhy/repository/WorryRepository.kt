package com.agc.worrywhy.repository

import com.agc.worrywhy.persistence.WorryDao
import com.agc.worrywhy.persistence.entity.Worry
import com.agc.worrywhy.persistence.relationship.WorryWithInstancesAndText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorryRepository @Inject constructor(
    private val worryDao: WorryDao
) {
    fun getAllWorries() = worryDao.getAllComplete()
    fun getWorry(worryId: Long) = worryDao.getWorryWithInstancesAndText(worryId)
    suspend fun addWorry(title: String) = worryDao.addWorry(Worry(title))
    suspend fun addWorryInstance(worryId: Long, whatHappened: String? = null) =
        worryDao.addWorryInstance(worryId, whatHappened)

    suspend fun updateTitle(newTitle: String, worryId: Long) = worryDao.updateTitle(newTitle, worryId)
    suspend fun deleteWorry(worryId: Long) = worryDao.deleteWorry(worryId)
    suspend fun deleteWorryInstance(instanceId: Long) = worryDao.deleteWorryInstance(instanceId)
    suspend fun deleteAll() = worryDao.deleteAll()

    fun getWorriesForMonth(month: YearMonth): Flow<MonthWorries> {
        return worryDao.getWorriesInMonth(month).map { monthWorries ->
            MonthWorries(month, monthWorries.map { day ->
                DayWorry(LocalDate.of(month.year, month.monthValue, day.day), day.count)
            })
        }
    }

//    fun getAllMonthWorries(): Flow<MonthWorries>
}

data class DayWorry(val day: LocalDate, val count: Int)
data class MonthWorries(val yearMonth: YearMonth, val worries: List<DayWorry>)

