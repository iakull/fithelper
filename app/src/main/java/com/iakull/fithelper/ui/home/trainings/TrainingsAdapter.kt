package com.iakull.fithelper.ui.home.trainings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.relation.DetailedTraining
import com.iakull.fithelper.data.relation.DetailedTrainingDiffCallback
import com.iakull.fithelper.databinding.ItemTrainingBinding
import com.iakull.fithelper.ui.common.DataBoundListAdapter

class TrainingsAdapter(
        clickCallback: ((DetailedTraining) -> Unit)?
) : DataBoundListAdapter<DetailedTraining, ItemTrainingBinding>(
        clickCallback,
        DetailedTrainingDiffCallback()
) {

    override fun createBinding(parent: ViewGroup): ItemTrainingBinding = ItemTrainingBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemTrainingBinding, item: DetailedTraining) {
        binding.training = item
    }
}