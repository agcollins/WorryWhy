package com.agc.worrywhy.persistence.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.agc.worrywhy.persistence.entity.WorryInstance
import com.agc.worrywhy.persistence.entity.WorryInstanceContext

data class WorryTextInstance(
    @Embedded
    val instance: WorryInstance,
    @Relation(
        parentColumn = "uid",
        entityColumn = "parentUid"
    )
    val text: List<WorryInstanceContext>
)
