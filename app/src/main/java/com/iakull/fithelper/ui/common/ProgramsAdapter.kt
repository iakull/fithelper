package com.iakull.fithelper.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.Program
import com.iakull.fithelper.data.model.ProgramDiffCallback
import com.iakull.fithelper.databinding.ItemProgramBinding

class ProgramsAdapter(clickCallback: ((Program) -> Unit)? = null) :
        DataBoundListAdapter<Program, ItemProgramBinding>(clickCallback, ProgramDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemProgramBinding = ItemProgramBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemProgramBinding, item: Program) {
        binding.program = item
    }
}