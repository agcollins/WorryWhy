package com.agc.worrywhy.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.agc.worrywhy.persistence.entity.Worry
import com.agc.worrywhy.persistence.entity.WorryInstance
import com.agc.worrywhy.persistence.entity.WorryInstanceContext
import com.agc.worrywhy.persistence.relationship.WorryWithInstancesAndText
import com.agc.worrywhy.persistence.view.CompleteWorry
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

@Dao
interface WorryDao {
    @Query("SELECT * FROM Worry")
    fun getAll(): Flow<List<Worry>>

    @Query("SELECT * FROM CompleteWorry")
    fun getAllComplete(): Flow<List<CompleteWorry>>

    @Transaction
    @Query("SELECT * FROM Worry")
    fun getWorriesWithInstancesAndText(): Flow<List<WorryWithInstancesAndText>>

    @Query("DELETE FROM Worry")
    suspend fun deleteAll()

    @Insert
    suspend fun addWorry(worry: Worry): Long

    @Transaction
    @Query("SELECT * FROM Worry WHERE uid = :worryId")
    fun getWorryWithInstancesAndText(worryId: Long): Flow<WorryWithInstancesAndText>

    @Insert
    suspend fun addWorryInstance(worryInstance: WorryInstance): Long

    @Insert
    suspend fun addWorryInstanceContext(worryInstanceContext: WorryInstanceContext): Long

    @Query("DELETE FROM Worry WHERE uid = :worryId")
    suspend fun deleteWorry(worryId: Long)

    @Query("DELETE FROM WorryInstance WHERE uid = (SELECT uid FROM WorryInstance ORDER BY date DESC)")
    suspend fun removeLatestWorryInstance()

    @Transaction
    suspend fun addWorryInstance(worryId: Long, context: String? = null) {
        val instanceId = addWorryInstance(WorryInstance(worryId))
        context?.let {
            addWorryInstanceContext(WorryInstanceContext(instanceId, it))
        }
    }

    @Transaction
    suspend fun addWorryWithInstance(worry: Worry, context: String? = null) {
        addWorryInstance(addWorry(worry), context)
    }

    @Query("DELETE FROM WorryInstance WHERE uid = :instanceId")
    suspend fun deleteWorryInstance(instanceId: Long)

    @Query("UPDATE Worry SET content = :newTitle WHERE uid = :worryId")
    suspend fun updateTitle(newTitle: String, worryId: Long)

    @Query(
        """
        SELECT day, COUNT(DISTINCT uid) AS count FROM (
            SELECT
                STRFTIME("%m", DATE(date/1000, 'unixepoch')) AS month,
                STRFTIME("%d", DATE(date/1000, 'unixepoch')) AS day,
                uid
            FROM WorryInstance
            WHERE month = :month
        ) GROUP BY day
    """
    )
    fun getWorriesInMonth(month: YearMonth): Flow<List<DayWorries>>

    @Query(
        """
        SELECT
            STRFTIME('%Y', date) AS year,
            STRFTIME('%m', date) AS month,
            STRFTIME('%d', date) AS day,
            COUNT(DISTINCT uid) AS count
        FROM (
            SELECT
                DATE(date/1000, 'unixepoch') AS date,
                uid
            FROM WorryInstance
        ) GROUP BY date
        """
    )
    fun getAllInstances(): Flow<List<YearDayWorries>>
}

data class YearDayWorries(val year: Int, val month: Int, val day: Int, val count: Int)
data class DayWorries(val day: Int, val count: Int)