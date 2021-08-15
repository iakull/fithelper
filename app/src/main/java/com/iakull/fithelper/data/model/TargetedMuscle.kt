package com.iakull.fithelper.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(indices = [Index(value = ["muscle"])],
        primaryKeys = ["exercise", "muscle"],
        foreignKeys = [
            ForeignKey(
                    entity = Exercise::class,
                    parentColumns = ["name"],
                    childColumns = ["exercise"],
                    onDelete = ForeignKey.CASCADE),
            ForeignKey(
                    entity = Muscle::class,
                    parentColumns = ["name"],
                    childColumns = ["muscle"],
                    onDelete = ForeignKey.CASCADE)
        ]
)
data class TargetedMuscle(
        val exercise: String,
        val muscle: String
)