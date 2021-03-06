package com.iakull.fithelper.ui.common.detailed_exercise

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.iakull.fithelper.R
import com.iakull.fithelper.databinding.FragmentDetailedExerciseBinding
import com.iakull.fithelper.util.popBackStack
import com.iakull.fithelper.util.setNewValue
import com.iakull.fithelper.util.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailedExerciseFragment : Fragment() {

    private val args: DetailedExerciseFragmentArgs by navArgs()
    private val vm: DetailedExerciseViewModel by viewModel()
    private lateinit var binding: FragmentDetailedExerciseBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        binding = FragmentDetailedExerciseBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm.exerciseName.setNewValue(args.exercise)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.exercise_detail, menu)
        vm.exercise.observe(viewLifecycleOwner) { exercise ->
            val item = menu.findItem(R.id.addToFavorite)
            item.isChecked = exercise.isFavorite
            updateFavoriteButton(item)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.addToFavorite -> {
            item.isChecked = !item.isChecked
            updateFavoriteButton(item)
            vm.setFavorite(item.isChecked)
            true
        }
        R.id.del_exr -> {
            vm.deleteExercise()
            toast(getString(R.string.exercise_deleted))
            popBackStack()
            true
        }
        else -> false
    }

    private fun updateFavoriteButton(item: MenuItem) {
        if (item.isChecked) {
            item.setIcon(R.drawable.ic_favorite)
        } else {
            item.setIcon(R.drawable.ic_favorite_border)
        }
    }
}
