package com.iakull.fithelper.ui.home.trainings.add_training_set

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.iakull.fithelper.databinding.DialogAddTrainingSetBinding
import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.util.hideKeyboard
import com.iakull.fithelper.util.popBackStack
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddTrainingSetDialog : BottomSheetDialogFragment() {

    private val args: AddTrainingSetDialogArgs by navArgs()
    private val vm: AddTrainingSetViewModel by viewModel()
    private val sharedVM: SharedViewModel by sharedViewModel()
    private lateinit var binding: DialogAddTrainingSetBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogAddTrainingSetBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm.start(
                args.trainingExerciseId,
                args.setId,
                args.weight,
                args.reps,
                args.time,
                args.distance
        )

        vm.trainingSetSavedEvent.observe(viewLifecycleOwner) {
            sharedVM.onSetCompleted(vm.trainingExercise.value?.rest ?: 0)
            hideKeyboard()
            popBackStack()
        }
    }
}