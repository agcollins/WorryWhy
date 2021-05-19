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

@DatabaseView("""
    SELECT uid, content, count
    FROM Worry
    JOIN (
        SELECT COUNT(*) as count, parentUid
        FROM WorryInstance
    )
    AS instances
    ON Worry.uid = instances.parentUid
""")
data class CompleteWorry(
    val uid: Long,
    val content: String,
    val count: Int
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Worry::class,
        parentColumns = ["uid"],
        childColumns = ["parentUid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
class WorryInstance(val parentUid: Int, val date: Date = Date()) {
    @PrimaryKey
    var uid: Int = 0
}

@Entity
class Worry(
    @ColumnInfo(name = "content")
    val content: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
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

@Database(entities = [Worry::class], version = 2)
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