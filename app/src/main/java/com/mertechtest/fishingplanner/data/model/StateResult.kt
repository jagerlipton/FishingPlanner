package com.mertechtest.fishingplanner.data.model

sealed class StateResult<T> {
    class Loading<T> : StateResult<T>()
    data class Success<T>(val data: T) : StateResult<T>()
    data class Error<T>(val message: String) : StateResult<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> error(message: String) = Error<T>(message)
    }
}