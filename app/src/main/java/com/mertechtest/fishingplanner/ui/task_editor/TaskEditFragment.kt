package com.mertechtest.fishingplanner.ui.task_editor

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mertechtest.fishingplanner.data.model.StateResult
import com.mertechtest.fishingplanner.data.model.TaskDBmodel
import com.mertechtest.fishingplanner.databinding.FragmentTaskEditBinding
import com.mertechtest.fishingplanner.ui.EventObserver
import com.mertechtest.fishingplanner.ui.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TaskEditFragment : Fragment() {

    private var _binding: FragmentTaskEditBinding? = null
    private val binding get() = _binding!!
    private var currentTask = TaskDBmodel()
    private val taskEditViewModel by viewModel<TaskEditViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTaskEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSave.setOnClickListener {
            addTask()
        }
        binding.buttonCancel.setOnClickListener {
            closeFragment()
        }

        binding.dateTextFieldText.setOnClickListener {
            showDatePickerDialog()
        }
        observers()
    }

    override fun onResume() {
        super.onResume()
        val id = arguments?.getInt("taskId") ?: -1
        if (id != -1) taskEditViewModel.loadTaskFromDb(id)
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val datetime: String = "$dayOfMonth.$monthOfYear.$year"
                binding.dateTextFieldText.setText(Utils.localTimeFormatter(datetime))
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun observers() {
        taskEditViewModel.currentTaskLiveData.observe(requireActivity(), EventObserver { state ->
            when (state) {
                is StateResult.Loading -> {
                }
                is StateResult.Success -> {
                    currentTask = state.data
                    with(binding) {
                        nameTextFieldText.setText(currentTask.name)
                        dateTextFieldText.setText(currentTask.dateTime?.let { it })
                        locationTextFieldText.setText(currentTask.city)
                        infoTextFieldText.setText(currentTask.info)
                    }
                }
                is StateResult.Error -> {
                    //
                }
            }
        })


        taskEditViewModel.weatherTaskLiveData.observe(requireActivity(), EventObserver { state ->
            when (state) {
                is StateResult.Loading -> {
                }
                is StateResult.Success -> {
                    closeFragment()
                }
                is StateResult.Error -> {
                    //
                }
            }
        })
    }

    private fun addTask() {
        val id = arguments?.getInt("taskId") ?: -1
        if (id!=-1) {
            val name: String = binding.nameTextFieldText.text.toString()
            val date: String = binding.dateTextFieldText.text.toString()
            val location: String = binding.locationTextFieldText.text.toString()
            val info: String = binding.infoTextFieldText.text.toString()
            taskEditViewModel.updateTaskToDB (id, name, date, location, info)
            }
            else {
                val name: String = binding.nameTextFieldText.text.toString()
                val date: String = binding.dateTextFieldText.text.toString()
                val location: String = binding.locationTextFieldText.text.toString()
                val info: String = binding.infoTextFieldText.text.toString()
                taskEditViewModel.getWeatherAndTask(name, date, location, info)
            }
    }

    private fun closeFragment() {
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}