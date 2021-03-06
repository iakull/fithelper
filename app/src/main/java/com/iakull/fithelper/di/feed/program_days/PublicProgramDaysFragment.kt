package com.iakull.fithelper.di.feed.program_days

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.iakull.fithelper.R
import com.iakull.fithelper.di.feed.program_days.PublicProgramDaysFragmentDirections.Companion.toPublicProgramDayExercisesFragment
import com.iakull.fithelper.ui.home.trainings.choose_program_day.ChooseProgramDayAdapter
import com.iakull.fithelper.util.live_data.EventObserver
import com.iakull.fithelper.util.navigate
import com.iakull.fithelper.util.setNewValue
import com.iakull.fithelper.util.setTitle
import com.iakull.fithelper.util.toast
import kotlinx.android.synthetic.main.fragment_public_program_days.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PublicProgramDaysFragment : Fragment() {

    private val args: PublicProgramDaysFragmentArgs by navArgs()

    private val vm: PublicProgramDaysViewModel by viewModel()

    private val adapter by lazy {
        ChooseProgramDayAdapter {
            navigate(toPublicProgramDayExercisesFragment(it.programId, it.id))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_public_program_days, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.adapter = adapter

        vm.programId.setNewValue(args.programId)
        vm.program.observe(viewLifecycleOwner) { setTitle(it.name) }
        vm.programDays.observe(viewLifecycleOwner) { adapter.submitList(it) }
        vm.programAddedToMyProgramsEvent.observe(viewLifecycleOwner, EventObserver {
            toast(getString(R.string.program_added_to_my_programs))
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.public_program, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.add_to_my_programs -> {
            vm.addToMyPrograms()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
