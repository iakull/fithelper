package com.iakull.fithelper.ui.home.programs.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.ProgramDayExercise
import com.iakull.fithelper.data.model.ProgramDayExerciseDiffCallback
import com.iakull.fithelper.databinding.ItemProgramExerciseBinding
import com.iakull.fithelper.ui.common.DataBoundListAdapter

class ProgramDayExercisesAdapter(
        clickCallback: ((ProgramDayExercise) -> Unit)? = null
) : DataBoundListAdapter<ProgramDayExercise, ItemProgramExerciseBinding>(clickCallback, ProgramDayExerciseDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemProgramExerciseBinding = ItemProgramExerciseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemProgramExerciseBinding, item: ProgramDayExercise) {
        binding.exercise = item
    }
}