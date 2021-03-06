package com.iakull.fithelper.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.iakull.fithelper.data.remote.generateId
import java.util.*

@Entity(indices = [Index(value = ["programDayId"])],
        foreignKeys = [
            ForeignKey(
                    entity = ProgramDay::class,
                    parentColumns = ["id"],
                    childColumns = ["programDayId"],
                    onDelete = ForeignKey.SET_NULL)
        ]
)
data class Training(
        val startDateTime: Date = Date(),
        val programDayId: String? = null,
        val duration: Int? = null,
        val lastUpdate: Date = Date(),
        val deleted: Boolean = false,
        @PrimaryKey val id: String = generateId()
)