package com.agc.worrywhy

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Entity
class Worry(
    @ColumnInfo(name = "content")
    val content: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "count")
    var count: Int = 0
}

@Dao
interface WorryDao {
    @Query("SELECT * FROM Worry")
    fun getAll(): Flow<List<Worry>>

    @Query("DELETE FROM Worry")
    suspend fun deleteAll()

    @Insert
    suspend fun addWorry(worry: Worry)
}

@Database(entities = [Worry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun worryDao(): WorryDao
}

