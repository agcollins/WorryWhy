package com.agc.worrywhy.repository

import com.agc.worrywhy.persistence.WorryDao
import com.agc.worrywhy.persistence.entity.Worry
import com.agc.worrywhy.persistence.relationship.WorryWithInstancesAndText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
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

    suspend fun updateTitle(newTitle: String, worryId: Long) =
        worryDao.updateTitle(newTitle, worryId)

    suspend fun deleteWorry(worryId: Long) = worryDao.deleteWorry(worryId)
    suspend fun deleteWorryInstance(instanceId: Long) = worryDao.deleteWorryInstance(instanceId)
    suspend fun deleteAll() = worryDao.deleteAll()

    // Assumption - dao returns instances with same year
    fun getAllMonthWorries(): Flow<List<MonthWorries>> {
        return worryDao.getAllInstances().map {
            val monthMap = it.groupBy { it.month }
            monthMap.keys.map { month ->
                val value = monthMap[month]!!
                val year = value.first().year
                MonthWorries(YearMonth.of(year, month), value.map {
                    DayWorry(LocalDate.of(it.year, it.month, it.day), it.count)
                })
            }
        }
    }

    fun getDayWorries(day: LocalDate): Flow<List<WorryWithInstancesAndText>> {
        return worryDao.getWorriesWithInstancesAndText().map { worries ->
            worries.map {
                WorryWithInstancesAndText(it.worry,
                    it.instances.filter { textInstance ->
                        textInstance.instance.date.toInstant().atZone(ZoneId.systemDefault())
                            .toLocalDate().isEqual(day)
                    }
                )
            }.filter {
                it.instances.isNotEmpty()
            }
        }
    }
}

data class DayWorry(val day: LocalDate, val count: Int)
data class MonthWorries(val yearMonth: YearMonth, val worries: List<DayWorry>)

