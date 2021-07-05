package com.agc.worrywhy.persistence

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.*

class Converters {
    @TypeConverter
    fun fromDate(date: Date): Long = date.time

    @TypeConverter
    fun fromLong(long: Long): Date = Date(long)

    @TypeConverter
    fun fromYearMonthToString(yearMonth: YearMonth): String {
        return yearMonth.monthValue.toString().padStart(2, '0')
    }

    @TypeConverter
    fun fromLongToLocalDate(long: Long): LocalDate {
        return Date(long).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }
}