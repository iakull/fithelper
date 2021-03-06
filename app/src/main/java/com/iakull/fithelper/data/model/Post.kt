package com.iakull.fithelper.data.model

import androidx.recyclerview.widget.DiffUtil
import com.iakull.fithelper.data.remote.generateId
import java.util.*

data class Post(
        val authorId: String? = null,
        val authorName: String? = null,
        val title: String? = null,
        val content: String? = null,
        val imageUrl: String? = null,
        val program: Program? = null,
        val timestamp: Date = Date(),
        val likes: List<String> = listOf(), // Ids of people who liked the post
        val id: String = generateId()
)

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
}