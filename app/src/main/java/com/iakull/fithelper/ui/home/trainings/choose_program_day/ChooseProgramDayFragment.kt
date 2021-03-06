package com.iakull.fithelper.ui.home.trainings.choose_program_day

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.iakull.fithelper.R
import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.ui.home.programs.program_days.ProgramDaysViewModel
import com.iakull.fithelper.util.live_data.Event
import com.iakull.fithelper.util.popBackStack
import com.iakull.fithelper.util.setNewValue
import kotlinx.android.synthetic.main.fragment_choose_program_day.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseProgramDayFragment : Fragment() {

    private val args: ChooseProgramDayFragmentArgs by navArgs()

    private val vm: ProgramDaysViewModel by viewModel()
    private val sharedVM: SharedViewModel by sharedViewModel()

    private val adapter by lazy {
        ChooseProgramDayAdapter { programDay ->
            sharedVM.programDay.value = Event(programDay)
            popBackStack(R.id.createTrainingDialog, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_program_day, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm.programId.setNewValue(args.programId)

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        vm.programDays.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}
