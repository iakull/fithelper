package com.iakull.fithelper.data.model

import androidx.recyclerview.widget.DiffUtil

data class User(
        val id: String = "",
        val name: String = "",
        val gender: String = "",
        val trainingTarget: String = "",
        val weight: String = "",
        val height: String = ""
)

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
}