package com.agc.worrywhy.persistence

import androidx.room.Embedded
import androidx.room.Relation

data class WorryTextInstance(
    @Embedded
    val instance: WorryInstance,
    @Relation(
        parentColumn = "uid",
        entityColumn = "parentUid"
    )
    val text: List<WorryInstanceContext>
)
