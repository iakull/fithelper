package com.iakull.fithelper.di.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.Post
import com.iakull.fithelper.data.model.PostDiffCallback
import com.iakull.fithelper.data.model.Program
import com.iakull.fithelper.databinding.ItemPostBinding
import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.ui.common.DataBoundListAdapter

class PostsAdapter(
        private val sharedVM: SharedViewModel,
        private val programClickCallback: ((Program) -> Unit),
        private val likeClickCallback: ((Post) -> Unit)
) : DataBoundListAdapter<Post, ItemPostBinding>(null, PostDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemPostBinding = ItemPostBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemPostBinding, item: Post) {
        binding.sharedVM = sharedVM
        binding.post = item

        item.program?.let { program ->
            binding.program.setOnClickListener { programClickCallback(program) }
        }
        binding.likeBtn.setOnClickListener { likeClickCallback(item) }
    }
}