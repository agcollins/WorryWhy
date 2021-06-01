package com.agc.worrywhy.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WorryDao {
    @Query("SELECT * FROM Worry")
    fun getAll(): Flow<List<Worry>>

    @Query("SELECT * FROM CompleteWorry")
    fun getAllComplete(): Flow<List<CompleteWorry>>

    @Transaction
    @Query("SELECT * FROM Worry")
    fun getWorriesWithInstances(): List<WorryWithInstances>

    @Query("DELETE FROM Worry")
    suspend fun deleteAll()

    @Insert
    suspend fun addWorry(worry: Worry): Long

    @Transaction
    @Query("SELECT * FROM Worry WHERE uid = :worryId")
    fun getWorryWithInstances(worryId: Long): Flow<WorryWithInstances>

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
}