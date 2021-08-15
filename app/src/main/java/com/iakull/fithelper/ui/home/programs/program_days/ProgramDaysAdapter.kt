package com.iakull.fithelper.ui.home.programs.program_days

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.ProgramDay
import com.iakull.fithelper.data.model.ProgramDayDiffCallback
import com.iakull.fithelper.databinding.ItemProgramDayDraggableBinding
import com.iakull.fithelper.ui.common.DataBoundListAdapter

class ProgramDaysAdapter(
        clickCallback: ((ProgramDay) -> Unit)? = null
) : DataBoundListAdapter<ProgramDay, ItemProgramDayDraggableBinding>(clickCallback, ProgramDayDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemProgramDayDraggableBinding = ItemProgramDayDraggableBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemProgramDayDraggableBinding, item: ProgramDay) {
        binding.day = item
    }
}