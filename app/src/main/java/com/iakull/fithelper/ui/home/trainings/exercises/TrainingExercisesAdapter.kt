package com.iakull.fithelper.ui.home.trainings.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.TrainingExercise
import com.iakull.fithelper.data.model.TrainingExerciseDiffCallback
import com.iakull.fithelper.databinding.ItemTrainingExerciseBinding
import com.iakull.fithelper.ui.common.DataBoundListAdapter

class TrainingExercisesAdapter(
        clickCallback: ((TrainingExercise) -> Unit),
        private val finishIconClickCallback: ((TrainingExercise) -> Unit)? = null
) : DataBoundListAdapter<TrainingExercise, ItemTrainingExerciseBinding>(
        clickCallback,
        TrainingExerciseDiffCallback()
) {

    override fun createBinding(parent: ViewGroup): ItemTrainingExerciseBinding = ItemTrainingExerciseBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemTrainingExerciseBinding, item: TrainingExercise) {
        binding.exercise = item
        //binding.dragFinishIcon.setOnClickListener { finishIconClickCallback?.invoke(item) }
    }
}