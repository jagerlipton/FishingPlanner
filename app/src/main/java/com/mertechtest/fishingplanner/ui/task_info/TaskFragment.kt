package com.mertechtest.fishingplanner.ui.task_info

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mertechtest.fishingplanner.App
import com.mertechtest.fishingplanner.R
import com.mertechtest.fishingplanner.data.model.StateResult
import com.mertechtest.fishingplanner.data.model.TaskDBmodel
import com.mertechtest.fishingplanner.databinding.FragmentTaskBinding
import com.mertechtest.fishingplanner.ui.EventObserver
import com.squareup.picasso.Picasso
import javax.inject.Inject

class TaskFragment : Fragment() {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    lateinit var taskViewModel: TaskViewModel

    @Inject
    lateinit var factory: TaskViewModelFactory

    private var currentTask = TaskDBmodel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (context?.applicationContext as App).appComponent.inject(this)

        taskViewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        binding.menuTaskFab.setOnClickListener {
            showPopup(binding.menuTaskFab)
        }

        observers()
    }

    override fun onResume() {
        super.onResume()
        val id = arguments?.getInt("taskId") ?: -1
        if (id != -1) taskViewModel.loadTaskFromDb(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observers() {
        taskViewModel.currentTaskLiveData.observe(requireActivity(), EventObserver { state ->
            when (state) {
                is StateResult.Loading -> {
                }
                is StateResult.Success -> {
                    currentTask = state.data
                    with(binding) {
                        nameSubTextView.text = currentTask.name
                        dateSubTextView.text =
                            currentTask.dateTime?.let { (it) }
                        locationSubTextView.text = currentTask.city
                        infoSubTextView.text = currentTask.info
                        weatherTempTextView.text = currentTask.temp.toString()
                        val iconCode = currentTask.icon?.replace("n", "d")
                        changeFragmentBg(iconCode)
                        Picasso.get()
                            .load("http://openweathermap.org/img/wn/" + "${iconCode}@4x.png")
                            .into(weatherImageView)
                        if (currentTask.completed) completedSubTextView.text = "Завершено"
                        else completedSubTextView.text = "Не завершено"
                    }

                }
                is StateResult.Error -> {
                    //
                }
            }
        })


    }

    private fun Context.getMyDrawable(id: Int): Drawable? {
        return ContextCompat.getDrawable(this, id)
    }

    private fun changeFragmentBg(iconCode: String?) {
        when (iconCode) {
            "01d", "02d", "03d" -> binding.taskConstraint.background =
                requireContext().getMyDrawable(R.drawable.sunny_gradient_bg)
            "04d", "09d", "10d", "11d" -> binding.taskConstraint.background =
                requireContext().getMyDrawable(R.drawable.rainy_gradient_bg)
            "13d", "50d" -> binding.taskConstraint.background =
                requireContext().getMyDrawable(R.drawable.snowly_gradient_bg)
        }
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.action_task)
        popup.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.comleted -> {
                    completeTask(currentTask)
                }
                R.id.change -> {
                    changeTask(currentTask)
                }
                R.id.delete -> {
                    deleteTask(currentTask.id.toInt())
                }
            }
            true
        }
        popup.show()
    }

    private fun completeTask(task: TaskDBmodel) {
        taskViewModel.completeTask(task)
        findNavController().navigateUp()
    }

    private fun changeTask(task: TaskDBmodel) {
        val bundle = Bundle()
        bundle.putInt("taskId", task.id.toInt())
        findNavController().navigate(R.id.action_taskFragment_to_taskEditFragment, bundle)
    }

    private fun deleteTask(id: Int) {
        taskViewModel.deleteTask(id)
        findNavController().navigateUp()
    }

}