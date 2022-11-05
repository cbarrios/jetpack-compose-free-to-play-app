package com.lalosapps.freetoplay.core.util

sealed interface Resource<out T> {
    object Loading : Resource<Nothing>
    data class Success<T>(val data: T) : Resource<T>
    data class Error<T>(val data: T? = null, val error: Throwable? = null) : Resource<T>
}