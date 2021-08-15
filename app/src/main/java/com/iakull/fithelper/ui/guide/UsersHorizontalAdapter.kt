package com.iakull.fithelper.ui.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.User
import com.iakull.fithelper.data.model.UserDiffCallback
import com.iakull.fithelper.databinding.ItemUserMiniBinding
import com.iakull.fithelper.ui.common.DataBoundListAdapter

class UsersHorizontalAdapter(
        clickCallback: ((User) -> Unit)? = null
) : DataBoundListAdapter<User, ItemUserMiniBinding>(clickCallback, UserDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemUserMiniBinding = ItemUserMiniBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemUserMiniBinding, item: User) {
        binding.user = item
    }
}