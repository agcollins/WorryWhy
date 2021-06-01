package com.agc.worrywhy.persistence

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

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