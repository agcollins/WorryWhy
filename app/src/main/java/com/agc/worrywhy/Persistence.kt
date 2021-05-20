package com.agc.worrywhy

import android.content.Context
import androidx.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Singleton

@DatabaseView(
    """
        SELECT uid, content, MAX(count) as count FROM (
            SELECT uid, content, count FROM Worry 
            CROSS JOIN (
                SELECT parentUid, COUNT(parentUid) AS count FROM WorryInstance GROUP BY parentUid
            ) WorryInstance
            ON Worry.uid = WorryInstance.parentUid
            UNION SELECT uid, content, 0 FROM Worry
        ) GROUP BY uid
"""
)
data class CompleteWorry(
    val uid: Long,
    val content: String,
    val count: Int
)

@Entity
data class Worry(
    @ColumnInfo(name = "content")
    val content: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}

@Entity(
    foreignKeys = [ForeignKey(
        entity = Worry::class,
        parentColumns = ["uid"],
        childColumns = ["parentUid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["parentUid"])
    ]
)
data class WorryInstance(val parentUid: Long, val date: Date = Date()) {
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}

data class WorryWithInstances(
    @Embedded val worry: Worry,
    @Relation(
        parentColumn = "uid",
        entityColumn = "parentUid"
    ) val instances: List<WorryInstance>
)

@Dao
interface WorryDao {
    @Query("SELECT * FROM Worry")
    fun getAll(): Flow<List<Worry>>

    @Query("SELECT * FROM CompleteWorry")
    fun getAllComplete(): Flow<List<CompleteWorry>>

    @Query("DELETE FROM Worry")
    suspend fun deleteAll()

    @Insert
    suspend fun addWorry(worry: Worry): Long

    @Insert
    suspend fun addWorryInstance(worryInstance: WorryInstance): Long

    @Query("DELETE FROM WorryInstance WHERE uid = (SELECT uid FROM WorryInstance ORDER BY date DESC)")
    suspend fun removeLatestWorryInstance()

    @Transaction
    suspend fun addWorryWithInstance(worry: Worry) {
        val worryInstance = WorryInstance(addWorry(worry))
        addWorryInstance(worryInstance)
    }
}

@Database(
    entities = [Worry::class, WorryInstance::class],
    views = [CompleteWorry::class],
    version = 7
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun worryDao(): WorryDao
}

class Converters {
    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun fromLong(long: Long): Date = Date(long)
}

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    @Provides
    @Singleton
    fun appDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun worryDao(appDatabase: AppDatabase): WorryDao = appDatabase.worryDao()
}