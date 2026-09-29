package com.example.basketmesaapp.model

/**
 * Estado de una carga de datos. Distingue "todavía cargando" de "no hay
 * datos" y de "ha fallado", que antes se confundían (un error se mostraba
 * como una lista vacía).
 */

sealed interface DataState<out T> {
    object Loading : DataState<Nothing>
    data class Success<T>(val data: T) : DataState<T>
    data class Error(val mensaje: String) : DataState<Nothing>
}

fun <T> DataState<T>.datosOrNull(): T? = (this as? DataState.Success<T>)?.data

fun <T> DataState<T>.errorOrNull(): String? = (this as? DataState.Error)?.mensaje