package com.iakull.fithelper.di.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.iakull.fithelper.databinding.FragmentFeedBinding
import com.iakull.fithelper.ui.SharedViewModel
import com.iakull.fithelper.di.feed.FeedFragmentDirections.Companion.toCreatePostFragment
import com.iakull.fithelper.di.feed.FeedFragmentDirections.Companion.toPublicProgramDaysFragment
import com.iakull.fithelper.util.navigate
import com.iakull.fithelper.util.setNewValue
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding

    private val postsAdapter by lazy {
        PostsAdapter(sharedVM,
                { navigate(toPublicProgramDaysFragment(it.id)) },
                { vm.likePost(it) })
    }

    private val vm: FeedViewModel by viewModel()
    private val sharedVM: SharedViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFeedBinding.inflate(inflater)
        binding.vm = vm
        binding.lifecycleOwner = this
        binding.postsRV.adapter = postsAdapter
        binding.fab.setOnClickListener { navigate(toCreatePostFragment()) }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        postsRV.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        sharedVM.user.observe(viewLifecycleOwner) { vm.user.setNewValue(it) }
        vm.posts.observe(viewLifecycleOwner) { postsAdapter.submitList(it) }
    }
}