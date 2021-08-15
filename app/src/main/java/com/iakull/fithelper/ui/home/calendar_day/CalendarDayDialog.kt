package com.iakull.fithelper.ui.home.calendar_day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iakull.fithelper.databinding.DialogCalendarDayBinding
import com.iakull.fithelper.ui.home.calendar_day.CalendarDayDialogDirections.Companion.toCreateTrainingDialog
import com.iakull.fithelper.ui.home.calendar_day.CalendarDayDialogDirections.Companion.toTrainingExercisesFragment
import com.iakull.fithelper.ui.home.trainings.TrainingsAdapter
import com.iakull.fithelper.util.navigate
import com.iakull.fithelper.util.setNewValue
import com.iakull.fithelper.util.toDate
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalendarDayDialog : BottomSheetDialogFragment() {

    private val args: CalendarDayDialogArgs by navArgs()
    private val vm: CalendarDayViewModel by viewModel()
    private lateinit var binding: DialogCalendarDayBinding

    private val adapter by lazy {
        TrainingsAdapter {
            navigate(toTrainingExercisesFragment(it.id, it.duration == null))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogCalendarDayBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = adapter
        binding.addTrainingButton.setOnClickListener { navigate(toCreateTrainingDialog(args.date)) }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm.date.setNewValue(args.date.toDate())
        vm.trainings.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}