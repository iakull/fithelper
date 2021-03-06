package com.iakull.fithelper.ui.home.trainings.create_training

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.iakull.fithelper.databinding.DialogCreateTrainingBinding
import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.ui.home.trainings.create_training.CreateTrainingDialogDirections.Companion.toChooseProgramFragment
import com.iakull.fithelper.ui.home.trainings.create_training.CreateTrainingDialogDirections.Companion.toExercisesFragment
import com.iakull.fithelper.util.*
import com.iakull.fithelper.util.live_data.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.Year
import org.threeten.bp.ZoneOffset

class CreateTrainingDialog : Fragment() {

    private val args: CreateTrainingDialogArgs by navArgs()
    private lateinit var binding: DialogCreateTrainingBinding
    private val vm: CreateTrainingViewModel by viewModel()
    private val sharedVM: SharedViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogCreateTrainingBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = this
        setDatePickerDialog()
        return binding.root
    }

    private fun setDatePickerDialog() {
        binding.dateTv.setOnClickListener {
            val dateTime = vm.dateTime.value!!
            val listener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                vm.dateTime.setNewValue(dateTime.withYear(year).withMonth(monthOfYear + 1).withDayOfMonth(dayOfMonth))
            }
            DatePickerDialog(requireContext(), listener, dateTime.year, dateTime.monthValue - 1, dateTime.dayOfMonth).apply {
                fun LocalDate.toEpochMilli() = atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
                datePicker.minDate = Year.of(2000).atDay(1).toEpochMilli()
                datePicker.maxDate = LocalDate.now().plusMonths(1).toEpochMilli()
                show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setXNavIcon()

        args.date?.let { vm.dateTime.setNewValue(it.toLocalDateTime()) }

        sharedVM.program.observe(viewLifecycleOwner, EventObserver { vm.program.setNewValue(it) })
        sharedVM.programDay.observe(viewLifecycleOwner, EventObserver { vm.programDay.setNewValue(it) })

        binding.startTrainingBtn.setOnClickListener {
            vm.createTraining()
            sharedVM.onTrainingSessionStarted()
        }
        binding.programLayout.setOnClickListener { navigate(toChooseProgramFragment()) }

        vm.trainingCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            hideKeyboard()
            navigate(toExercisesFragment(it, true))
        })
    }
}
