package com.agc.worrywhy.persistence

import androidx.room.DatabaseView

@DatabaseView(
    """
        SELECT uid, content, MAX(count) AS count FROM (
            SELECT uid, content, count FROM Worry 
            CROSS JOIN (
                SELECT parentUid, COUNT(parentUid) AS count FROM WorryInstance
                GROUP BY parentUid
            ) WorryInstance
            ON Worry.uid = WorryInstance.parentUid
            UNION ALL
                SELECT uid, content, 0 FROM Worry
        ) GROUP BY uid
"""
)
data class CompleteWorry(
    val uid: Long,
    val content: String,
    val count: Int
)