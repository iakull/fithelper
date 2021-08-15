package com.iakull.fithelper.ui.common.create_exercise

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.iakull.fithelper.databinding.FragmentCreateExerciseBinding
import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.util.dispatchGetImageContentIntent
import com.iakull.fithelper.util.popBackStack
import com.iakull.fithelper.util.setNewValue
import kotlinx.android.synthetic.main.fragment_create_exercise.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val RC_PHOTO_PICKER = 1

class CreateExerciseFragment : Fragment() {

    private val vm: CreateExerciseViewModel by viewModel()
    private val sharedVM: SharedViewModel by sharedViewModel()
    private lateinit var binding: FragmentCreateExerciseBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCreateExerciseBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = this
        //binding.addMuscleButton.setOnClickListener { navigate(toChooseMuscleFragment()) }
        binding.addImageButton.setOnClickListener { dispatchGetImageContentIntent(RC_PHOTO_PICKER) }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedVM.user.observe(viewLifecycleOwner) { vm.user.setNewValue(it) }
        //sharedVM.program.observe(viewLifecycleOwner, EventObserver { vm.program.setNewValue(it) })
        binding.addBtn.setOnClickListener {
            /*if (validate())*/ vm.createExr()
            popBackStack()
        }
    }

    private fun validate(): Boolean {
        val nameIsEmpty = vm.name.value?.trim()?.isEmpty() ?: true
        name_exer.error = "Заполните поле".takeIf { nameIsEmpty }
        return !nameIsEmpty
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            vm.imageUri.setNewValue(data?.data)
        }
    }
}