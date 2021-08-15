package com.iakull.fithelper.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.User
import com.iakull.fithelper.data.model.UserDiffCallback
import com.iakull.fithelper.databinding.ItemUserBinding

class UsersVerticalAdapter(
        clickCallback: ((User) -> Unit)? = null
) : DataBoundListAdapter<User, ItemUserBinding>(clickCallback, UserDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemUserBinding = ItemUserBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemUserBinding, item: User) {
        binding.user = item
    }
}