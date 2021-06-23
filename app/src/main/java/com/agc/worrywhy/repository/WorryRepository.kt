package com.agc.worrywhy.repository

import com.agc.worrywhy.persistence.WorryDao
import com.agc.worrywhy.persistence.entity.Worry
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
}