package com.agc.worrywhy

import android.content.Context
import androidx.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton


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
        ).build()

    @Provides
    @Singleton
    fun worryDao(appDatabase: AppDatabase): WorryDao = appDatabase.worryDao()
}