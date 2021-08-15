package com.iakull.fithelper.ui.common.exercises

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.iakull.fithelper.R
import com.iakull.fithelper.databinding.FragmentExercisesBinding
import com.iakull.fithelper.ui.common.ChipGroupFilterAdapter
import com.iakull.fithelper.ui.common.exercises.ExercisesFragmentDirections.Companion.toExerciseDetailFragment
import com.iakull.fithelper.util.*
import com.iakull.fithelper.util.live_data.EventObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExercisesFragment : Fragment() {

    private val args: ExercisesFragmentArgs by navArgs()
    private val vm: ExercisesViewModel by viewModel()
    private lateinit var binding: FragmentExercisesBinding
    private lateinit var exerciseTargetAdapter: ChipGroupFilterAdapter

    private val exerciseAdapter by lazy {
        ExercisesAdapter(vm) { exercise ->
            hideKeyboard()
            when {
                args.programDayId != null -> vm.addExerciseToProgramDay(exercise, args.programDayId!!, args.num)
                args.trainingId != null -> vm.addExerciseToTraining(exercise, args.trainingId!!, args.num)
                else -> navigate(toExerciseDetailFragment(exercise.name))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentExercisesBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = this
        binding.recyclerView.adapter = exerciseAdapter
        binding.addExercise.setOnClickListener { navigate(ExercisesFragmentDirections.toCreateExerciseFragment()) }

        exerciseTargetAdapter = ChipGroupFilterAdapter(binding.targetsCG) { id, isChecked ->
            vm.setChecked(vm.exerciseTargetList, id, isChecked)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (args.num != -1) setXNavIcon()
        vm.exerciseList.observe(viewLifecycleOwner) { exerciseAdapter.submitList(it) }
        //vm.exerciseTargetList.observe(viewLifecycleOwner) { exerciseTargetAdapter.submitList(it) }
        vm.exerciseAddedEvent.observe(viewLifecycleOwner, EventObserver { popBackStack() })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_filter, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView

        val query = vm.searchText.value
        if (query?.trim()?.isNotEmpty() == true) {
            searchView.setQuery(query, false)
            searchView.isIconified = false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                vm.searchText.setNewValue(newText)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.filter -> {
            vm.updateQuery()
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}