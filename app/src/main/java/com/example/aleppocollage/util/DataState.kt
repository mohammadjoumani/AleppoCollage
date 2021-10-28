package com.example.aleppocollage.util


sealed class DataState<out R> {
    data class Success<out T>(val data: T): DataState<T>()
    data class Failure(val message: String): DataState<Nothing>()
    data class ExceptionState(val exception: Exception): DataState<Nothing>()
    object Loading: DataState<Nothing>()
}