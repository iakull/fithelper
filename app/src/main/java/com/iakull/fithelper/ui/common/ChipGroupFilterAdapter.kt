package com.iakull.fithelper.ui.common

import android.view.LayoutInflater
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.iakull.fithelper.R
import com.iakull.fithelper.data.relation.FilterParam

class ChipGroupFilterAdapter(private val group: ChipGroup, private val callback: ((String, Boolean) -> Unit)) {

    fun submitList(newList: List<FilterParam>) {
        group.removeAllViews()
        for (item in newList) {
            val chip = LayoutInflater.from(group.context).inflate(R.layout.view_chip_filter, group, false) as Chip
            chip.text = item.name
            chip.isChecked = item.isActive
            chip.setOnCheckedChangeListener { _, isChecked -> callback(item.name, isChecked) }
            group.addView(chip)
        }
    }
}