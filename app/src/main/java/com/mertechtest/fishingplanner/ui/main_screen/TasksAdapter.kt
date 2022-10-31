package com.mertechtest.fishingplanner.ui.main_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mertechtest.fishingplanner.data.model.TaskDBmodel
import com.mertechtest.fishingplanner.databinding.RecyclerviewItemBinding
import com.squareup.picasso.Picasso

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.ViewHolder>() {

    private val taskList = ArrayList<TaskDBmodel>()
    var onItemClick: ((TaskDBmodel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(taskList[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(taskList[position])
        }
    }

    override fun getItemCount(): Int = taskList.size

    fun setData(
        inputTaskList: List<TaskDBmodel>
    ) {
        taskList.clear()
        taskList.addAll(inputTaskList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: RecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(task: TaskDBmodel) {
            binding.apply {
                nameTextView.text = task.name
                dateTextView.text = task.dateTime?.let { it }
                if (task.completed) completedTextView.text = "Завершено"
                else completedTextView.text = "Не завершено"
                val iconCode = task.icon?.replace("n", "d")
                Picasso.get().load("http://openweathermap.org/img/wn/" + "${iconCode}@4x.png")
                    .into(binding.iconImageView)
                textCityName.text = task.city
                iconTempTextView.text = task.temp.toString()
            }
        }
    }

}