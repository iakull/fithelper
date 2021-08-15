package com.iakull.fithelper.data.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [
    Index(value = ["muscleFK"])
],
        foreignKeys = [
            ForeignKey(
                    entity = Muscle::class,
                    parentColumns = ["name"],
                    childColumns = ["muscleFK"],
                    onDelete = ForeignKey.SET_NULL)
        ]
)
data class Exercise(
        @PrimaryKey val name: String,
        val description: String = "",
        val imageUrl: String? = null,
        val muscleFK: String? = null,
        val executionsCount: Long = 0,
        val isFavorite: Boolean = false,
        val measuredInWeight: Boolean = true,
        val measuredInReps: Boolean = true,
        val measuredInTime: Boolean = true,
        val measuredInDistance: Boolean = true
)

class ExerciseDiffCallback : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise) = oldItem == newItem
}