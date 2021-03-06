package com.iakull.fithelper.ui.home.trainings.training_sets

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.iakull.fithelper.R
import com.iakull.fithelper.data.model.TrainingExercise
import com.iakull.fithelper.databinding.FragmentTrainingSetsBinding
import com.iakull.fithelper.ui.home.trainings.training_sets.TrainingSetsFragmentDirections.Companion.toAddSetDialog
import com.iakull.fithelper.util.live_data.EventObserver
import com.iakull.fithelper.util.navigate
import com.iakull.fithelper.util.popBackStack
import com.iakull.fithelper.util.setNewValue
import com.iakull.fithelper.util.setTitle
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingSetsFragment : Fragment() {

    private val args: TrainingSetsFragmentArgs by navArgs()

    private val vm: TrainingSetsViewModel by viewModel()

    private lateinit var binding: FragmentTrainingSetsBinding

    private val adapter by lazy {
        TrainingSetsAdapter { set ->
            val weight = set.weight ?: set.prevWeight ?: -1
            val reps = set.reps ?: set.prevReps ?: -1
            val time = set.time ?: set.prevTime ?: -1
            val distance = set.distance ?: set.prevDistance ?: -1

            navigate(toAddSetDialog(args.trainingExerciseId, set.id, weight, reps, time, distance))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentTrainingSetsBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = this

        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val set = adapter.getItem(viewHolder.adapterPosition)
                vm.deleteSet(set.id)
            }
        }).attachToRecyclerView(binding.recyclerView)

        binding.fab.setOnClickListener {
            val set = vm.currentSets.value?.lastOrNull() ?: vm.previousSets.value?.firstOrNull()

            val exercise = vm.exercise.value!!
            val weight = if (exercise.measuredInWeight) {
                set?.weight ?: 0
            } else -1
            val reps = if (exercise.measuredInReps) {
                set?.reps ?: 0
            } else -1
            val time = if (exercise.measuredInTime) {
                set?.time ?: 0
            } else -1
            val distance = if (exercise.measuredInDistance) {
                set?.distance ?: 0
            } else -1

            navigate(toAddSetDialog(args.trainingExerciseId, null, weight, reps, time, distance))
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm.trainingExerciseId.setNewValue(args.trainingExerciseId)
        vm.trainingExercise.observe(viewLifecycleOwner) {
            setTitle(it.exercise)
            activity?.invalidateOptionsMenu()
        }
        vm.sets.observe(viewLifecycleOwner) { adapter.submitList(it) }
        vm.trainingExerciseFinishedEvent.observe(viewLifecycleOwner, EventObserver { popBackStack() })
        vm.trainingExerciseDeletedEvent.observe(viewLifecycleOwner, EventObserver { popBackStack() })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.training_sets, menu)
        val exerciseIsRunning = vm.trainingExercise.value?.state == TrainingExercise.RUNNING
        menu.findItem(R.id.finish_exercise)?.isVisible = exerciseIsRunning
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.finish_exercise -> {
            vm.finishExercise()
            true
        }
        R.id.delete_exercise -> {
            vm.deleteExercise()
            true
        }
        else -> false
    }
}