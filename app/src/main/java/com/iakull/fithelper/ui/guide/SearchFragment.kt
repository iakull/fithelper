package com.iakull.fithelper.ui.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.iakull.fithelper.databinding.FragmentHomeBinding
import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.ui.common.ProgramsAdapter
import com.iakull.fithelper.di.feed.PostsAdapter
import com.iakull.fithelper.ui.guide.SearchFragmentDirections.Companion.toExerciseDetailFragment
import com.iakull.fithelper.ui.guide.SearchFragmentDirections.Companion.toExercisesFragment
import com.iakull.fithelper.ui.guide.SearchFragmentDirections.Companion.toPublicProgramDaysFragment
import com.iakull.fithelper.ui.guide.SearchFragmentDirections.Companion.toPublicProgramsFragment
import com.iakull.fithelper.util.navigate
import com.iakull.fithelper.util.setNewValue
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val peopleAdapter by lazy {
        UsersHorizontalAdapter {
            //navigate(toPersonPageFragment(it.id))
        }
    }
    private val exercisesAdapter by lazy {
        ExercisesHorizontalAdapter {
            navigate(toExerciseDetailFragment(it.name))
        }
    }
    private val programsAdapter by lazy {
        ProgramsAdapter {
            navigate(toPublicProgramDaysFragment(it.id))
        }
    }
    private val postsAdapter by lazy {
        PostsAdapter(sharedVM,
                { navigate(toPublicProgramDaysFragment(it.id)) },
                { vm.likePost(it) })
    }

    private val vm: SearchViewModel by viewModel()
    private val sharedVM: SharedViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = this
        binding.exercisesLabel.setOnClickListener { navigate(toExercisesFragment()) }
        binding.exercisesRV.adapter = exercisesAdapter
        binding.programsLabel.setOnClickListener { navigate(toPublicProgramsFragment()) }
        binding.programsRV.adapter = programsAdapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedVM.user.observe(viewLifecycleOwner) { vm.user.setNewValue(it) }
        vm.people.observe(viewLifecycleOwner) { peopleAdapter.submitList(it) }
        vm.exercises.observe(viewLifecycleOwner) { exercisesAdapter.submitList(it) }
        vm.programs.observe(viewLifecycleOwner) { programsAdapter.submitList(it) }
        vm.posts.observe(viewLifecycleOwner) { postsAdapter.submitList(it) }
    }
}