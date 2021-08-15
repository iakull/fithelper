package com.iakull.fithelper.di.feed.program_day_exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.iakull.fithelper.R
import com.iakull.fithelper.ui.home.programs.exercises.ProgramDayExercisesAdapter
import com.iakull.fithelper.util.setTitle
import kotlinx.android.synthetic.main.fragment_public_program_day_exercises.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PublicProgramDayExercisesFragment : Fragment() {

    private val args: PublicProgramDayExercisesFragmentArgs by navArgs()

    private val vm: PublicProgramDayExercisesViewModel by viewModel()

    private val adapter by lazy { ProgramDayExercisesAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_public_program_day_exercises, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.adapter = adapter

        vm.start(args.programId, args.programDayId)
        vm.programDay.observe(viewLifecycleOwner) { setTitle(it.name) }
        vm.exercises.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }
}
