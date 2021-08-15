package com.iakull.fithelper.ui.common.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.Exercise
import com.iakull.fithelper.data.model.ExerciseDiffCallback
import com.iakull.fithelper.databinding.ItemExerciseBinding
import com.iakull.fithelper.ui.common.DataBoundListAdapter

class ExercisesAdapter(
        private val viewModel: ExercisesViewModel,
        clickCallback: ((Exercise) -> Unit)? = null
) : DataBoundListAdapter<Exercise, ItemExerciseBinding>(clickCallback, ExerciseDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemExerciseBinding = ItemExerciseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemExerciseBinding, item: Exercise) {
        binding.exercise = item
        binding.vm = viewModel
    }
}