package com.iakull.fithelper.ui.common.choose_muscle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.iakull.fithelper.R
import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.ui.common.ProgramsAdapter
import com.iakull.fithelper.ui.common.choose_program.ChooseProgramViewModel
import com.iakull.fithelper.util.live_data.Event
import com.iakull.fithelper.util.navigate
import com.iakull.fithelper.util.popBackStack
import com.iakull.fithelper.util.setXNavIcon
import kotlinx.android.synthetic.main.fragment_choose_program.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseMuscleFragment : Fragment() {

   // private val args: ChooseMuscleFragmentArgs by navArgs()
    private val vm: ChooseProgramViewModel by viewModel()
    private val sharedVM: SharedViewModel by sharedViewModel()

    /*private val adapter by lazy {
        ProgramsAdapter { program ->
            when {
                args.publishProgram -> {
                    //vm.publishProgram(program)
                    //vm.programPublishedEvent.observe(viewLifecycleOwner) { popBackStack() }
                }
                args.toChooseMuscle -> {
                    //navigate(toChooseMuscleFragment(program.id))
                }
                else -> {
                    sharedVM.program.value = Event(program)
                    popBackStack()
                }
            }
        }*/
    //}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_program, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setXNavIcon()
        //recyclerView.adapter = adapter
        //vm.programs.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}
