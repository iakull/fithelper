package com.iakull.fithelper.di.feed.program_day_exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.ProgramDayExercise
import com.iakull.fithelper.data.model.ProgramDayExerciseDiffCallback
import com.iakull.fithelper.databinding.ItemPublicProgramExerciseBinding
import com.iakull.fithelper.ui.common.DataBoundListAdapter

class PublicProgramDayExercisesAdapter(
        clickCallback: ((ProgramDayExercise) -> Unit)? = null
) : DataBoundListAdapter<ProgramDayExercise, ItemPublicProgramExerciseBinding>(clickCallback, ProgramDayExerciseDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemPublicProgramExerciseBinding = ItemPublicProgramExerciseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemPublicProgramExerciseBinding, item: ProgramDayExercise) {
        binding.exercise = item
    }
}