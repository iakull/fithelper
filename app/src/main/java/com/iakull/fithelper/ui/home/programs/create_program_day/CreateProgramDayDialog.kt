package com.iakull.fithelper.ui.home.programs.create_program_day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iakull.fithelper.databinding.DialogCreateProgramDayBinding
import com.iakull.fithelper.ui.home.programs.create_program_day.CreateProgramDayDialogDirections.Companion.toProgramDayExercisesFragment
import com.iakull.fithelper.util.hideKeyboard
import com.iakull.fithelper.util.live_data.EventObserver
import com.iakull.fithelper.util.navigate
import kotlinx.android.synthetic.main.dialog_create_program_day.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateProgramDayDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCreateProgramDayBinding

    private val args: CreateProgramDayDialogArgs by navArgs()

    private val vm: CreateProgramDayViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogCreateProgramDayBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        descriptionET.setOnFocusChangeListener { _, hasFocus ->
            descriptionTIL.isCounterEnabled = hasFocus
        }

        binding.createProgramDayBtn.setOnClickListener {
            if (validate()) vm.createProgramDay(args.programId, args.num)
        }

        vm.programDayCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            hideKeyboard()
            navigate(toProgramDayExercisesFragment(it))
        })
    }

    private fun validate(): Boolean {
        val nameIsEmpty = nameTIL.editText?.text.toString().trim().isEmpty()
        nameTIL.error = "Заполните поле".takeIf { nameIsEmpty }
        return !nameIsEmpty
    }
}
