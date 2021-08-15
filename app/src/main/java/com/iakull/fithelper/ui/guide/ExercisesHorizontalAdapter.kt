package com.iakull.fithelper.ui.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.model.Exercise
import com.iakull.fithelper.data.model.ExerciseDiffCallback
import com.iakull.fithelper.databinding.ItemExerciseMiniBinding
import com.iakull.fithelper.ui.common.DataBoundListAdapter

class ExercisesHorizontalAdapter(
        clickCallback: ((Exercise) -> Unit)? = null
) : DataBoundListAdapter<Exercise, ItemExerciseMiniBinding>(clickCallback, ExerciseDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemExerciseMiniBinding = ItemExerciseMiniBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemExerciseMiniBinding, item: Exercise) {
        binding.exercise = item
    }
}