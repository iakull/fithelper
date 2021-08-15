package com.iakull.fithelper.ui.home.trainings.choose_program_day

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.ProgramDay
import com.iakull.fithelper.data.model.ProgramDayDiffCallback
import com.iakull.fithelper.databinding.ItemProgramDayBinding
import com.iakull.fithelper.ui.common.DataBoundListAdapter

class ChooseProgramDayAdapter(
        clickCallback: ((ProgramDay) -> Unit)? = null
) : DataBoundListAdapter<ProgramDay, ItemProgramDayBinding>(clickCallback, ProgramDayDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemProgramDayBinding = ItemProgramDayBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemProgramDayBinding, item: ProgramDay) {
        binding.day = item
    }
}