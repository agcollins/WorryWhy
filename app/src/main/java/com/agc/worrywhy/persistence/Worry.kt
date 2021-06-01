package com.agc.worrywhy.persistence

import androidx.room.*

@Entity
data class Worry(val content: String) {
    @PrimaryKey(autoGenerate = true)
    var uid: Long = 0
}

