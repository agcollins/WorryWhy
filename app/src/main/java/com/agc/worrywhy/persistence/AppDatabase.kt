package com.agc.worrywhy.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.agc.worrywhy.persistence.entity.Worry
import com.agc.worrywhy.persistence.entity.WorryInstance
import com.agc.worrywhy.persistence.entity.WorryInstanceContext
import com.agc.worrywhy.persistence.view.CompleteWorry

@Database(
    entities = [Worry::class, WorryInstance::class, WorryInstanceContext::class],
    views = [CompleteWorry::class],
    version = 10
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun worryDao(): WorryDao
}