package com.agc.worrywhy.persistence.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.agc.worrywhy.persistence.entity.Worry
import com.agc.worrywhy.persistence.entity.WorryInstance

data class WorryWithInstancesAndText(
    @Embedded val worry: Worry,
    @Relation(
        parentColumn = "uid",
        entityColumn = "parentUid",
        entity = WorryInstance::class
    ) val instances: List<WorryTextInstance>
)