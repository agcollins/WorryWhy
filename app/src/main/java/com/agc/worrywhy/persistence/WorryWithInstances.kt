package com.agc.worrywhy.persistence

import androidx.room.Embedded
import androidx.room.Relation

data class WorryWithInstances(
    @Embedded val worry: Worry,
    @Relation(
        parentColumn = "uid",
        entityColumn = "parentUid"
    ) val instances: List<WorryInstance>
)