package com.iakull.fithelper.data.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Muscle(@PrimaryKey val name: String)

class MuscleDiffCallback : DiffUtil.ItemCallback<Muscle>() {
    override fun areItemsTheSame(oldItem: Muscle, newItem: Muscle) = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: Muscle, newItem: Muscle) = oldItem == newItem
}