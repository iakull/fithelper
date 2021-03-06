package com.iakull.fithelper.data.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.iakull.fithelper.data.remote.generateId
import java.util.*

@Entity(indices = [Index(value = ["programDayId"]), Index(value = ["exercise"])],
        foreignKeys = [
            ForeignKey(
                    entity = ProgramDay::class,
                    parentColumns = ["id"],
                    childColumns = ["programDayId"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = Exercise::class,
                    parentColumns = ["name"],
                    childColumns = ["exercise"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class ProgramDayExercise(
        val programDayId: String = "",
        val exercise: String = "",
        val indexNumber: Int = 0,
        val rest: Int = 120,
        val lastUpdate: Date = Date(),
        val deleted: Boolean = false,
        @PrimaryKey val id: String = generateId()
)

class ProgramDayExerciseDiffCallback : DiffUtil.ItemCallback<ProgramDayExercise>() {
    override fun areItemsTheSame(oldItem: ProgramDayExercise, newItem: ProgramDayExercise) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: ProgramDayExercise, newItem: ProgramDayExercise) = oldItem == newItem
}