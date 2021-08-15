package com.iakull.fithelper.ui.home.trainings.training_sets

import android.view.LayoutInflater
import android.view.ViewGroup
import com.iakull.fithelper.data.relation.SetWithPreviousResults
import com.iakull.fithelper.data.relation.SetWithPreviousResultsDiffCallback
import com.iakull.fithelper.databinding.ItemTrainingSetBinding
import com.iakull.fithelper.ui.common.DataBoundListAdapter
import com.iakull.fithelper.ui.common.DataBoundViewHolder

class TrainingSetsAdapter(
        clickCallback: ((SetWithPreviousResults) -> Unit)
) : DataBoundListAdapter<SetWithPreviousResults, ItemTrainingSetBinding>(
        clickCallback,
        SetWithPreviousResultsDiffCallback()
) {

    override fun createBinding(parent: ViewGroup): ItemTrainingSetBinding = ItemTrainingSetBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun onBindViewHolder(holder: DataBoundViewHolder<ItemTrainingSetBinding>, position: Int) {
        holder.binding.num.text = (position + 1).toString()
        super.onBindViewHolder(holder, position)
    }

    override fun bind(binding: ItemTrainingSetBinding, item: SetWithPreviousResults) {
        binding.set = item
    }
}