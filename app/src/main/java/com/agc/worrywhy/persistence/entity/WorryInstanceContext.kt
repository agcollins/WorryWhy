package com.agc.worrywhy.persistence.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = WorryInstance::class,
        parentColumns = ["uid"],
        childColumns = ["parentUid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["parentUid"])
    ]
)
data class WorryInstanceContext(
    val parentUid: Long,
    val content: String
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}