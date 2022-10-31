package com.mertechtest.fishingplanner.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    @SuppressLint("SimpleDateFormat")
    fun unixTimeToLocal(inputDate: Long): String {
        return try {
            val oldFormat = SimpleDateFormat("dd.mm.yyyy")
            val date = Date(inputDate * 1000)
            oldFormat.format(date)
        } catch (e: Exception) {
            e.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun localTimeToUnix(inputDate: String): String {
        return try {
            val oldFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val newFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = oldFormat.parse(inputDate)
            val result = newFormat.format(date)
            Timestamp.valueOf(result).toInstant().toEpochMilli().toString()
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun localTimeFormatter(InputDate: String): String {
        val oldDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = oldDateFormat.parse(InputDate)
        val result = oldDateFormat.format(date)
        return result.toString()
    }
}