package com.mertechtest.fishingplanner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mertechtest.fishingplanner.data.model.TaskDBmodel.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class TaskDBmodel(
    @PrimaryKey (autoGenerate = true)
    var id: Long = 0,
    var name: String? = null,
    var dateTime: String? = null,
    var temp: Double? = null,
    var icon: String? = null,
    var city: String? = null,
    var country: String? = null,
    var info: String? = null,
    var completed: Boolean = false

) {
    companion object {
        const val TABLE_NAME = "fishing_planner"
    }
}