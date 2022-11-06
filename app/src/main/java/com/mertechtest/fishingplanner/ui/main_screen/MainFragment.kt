package com.mertechtest.fishingplanner.ui.main_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mertechtest.fishingplanner.App
import com.mertechtest.fishingplanner.R
import com.mertechtest.fishingplanner.data.model.StateResult
import com.mertechtest.fishingplanner.data.model.TaskDBmodel
import com.mertechtest.fishingplanner.databinding.FragmentMainBinding
import com.mertechtest.fishingplanner.ui.EventObserver
import javax.inject.Inject


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var factory: MainViewModelFactory

    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context?.applicationContext as App).appComponent.inject(this)

        mainViewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        binding.addTaskFab.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_taskEditFragment)
        }

        binding.swipeContainer.setOnRefreshListener {
            mainViewModel.refreshAllTasksFromDb()
            binding.swipeContainer.isRefreshing = false
        }

        createRecyclerView()
        mainViewModel.loadAllTasksFromDb()
        observers()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createRecyclerView() {
        tasksAdapter = TasksAdapter()
        tasksAdapter.onItemClick = { it ->
            showTaskWindow(it)
        }

        val mLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.taskRecyclerView.apply {
            layoutManager = mLayoutManager
            itemAnimator = DefaultItemAnimator()
            adapter = tasksAdapter
        }
    }


    private fun showTaskWindow(task: TaskDBmodel) {
        val bundle = Bundle()
        bundle.putInt("taskId", task.id.toInt())
        findNavController().navigate(R.id.action_mainFragment_to_taskFragment, bundle)
    }

    @SuppressLint("SetTextI18n")
    private fun observers() {
        mainViewModel.taskListLiveData.observe(requireActivity(), EventObserver { state ->
            when (state) {
                is StateResult.Loading -> {
                }
                is StateResult.Success -> {
                    if (state.data.isEmpty()) {
                        tasksAdapter.setData(listOf<TaskDBmodel>())
                    } else {
                        tasksAdapter.setData(state.data)
                    }
                }
                is StateResult.Error -> {
                    //
                }
            }
        })
    }


}




