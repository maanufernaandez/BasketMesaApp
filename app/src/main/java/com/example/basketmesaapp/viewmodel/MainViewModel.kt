package com.example.basketmesaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basketmesaapp.model.DesplazamientoRemoto
import com.example.basketmesaapp.model.DietaRemota
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.Sancion
import com.example.basketmesaapp.model.TarifaReglaRemota
import com.example.basketmesaapp.repository.FirestoreRepository
import com.example.basketmesaapp.utils.DataConstants
import com.example.basketmesaapp.utils.TarifaCalculator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Centraliza el acceso a Firestore y el estado de la pantalla principal.
 * Sobrevive a las recomposiciones y a los cambios de configuración (por
 * ejemplo, girar la pantalla), a diferencia del estado que vivía antes
 * directamente en el Composable.
 */
class MainViewModel(
    private val repository: FirestoreRepository = FirestoreRepository()
) : ViewModel() {

    val partidos: StateFlow<List<Partido>?> = repository.getPartidos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val sanciones: StateFlow<List<Sancion>?> = repository.getSanciones()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val reglasTarifa: StateFlow<List<TarifaReglaRemota>> = repository.getReglasTarifa()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val reglasDesplazamiento: StateFlow<List<DesplazamientoRemoto>> = repository.getDesplazamientos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val reglasDietas: StateFlow<List<DietaRemota>> = repository.getDietas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _userRol = MutableStateFlow("Oficial de Mesa")
    val userRol: StateFlow<String> = _userRol.asStateFlow()

    private val _autorizado3Vistas = MutableStateFlow(false)
    val autorizado3Vistas: StateFlow<Boolean> = _autorizado3Vistas.asStateFlow()

    /** Mensajes de error puntuales (para mostrar como Toast) que la UI escucha una sola vez. */
    private val _errores = MutableSharedFlow<String>()
    val errores: SharedFlow<String> = _errores

    init {
        sembrarDatosSiVacio()
        escucharPerfilUsuario()
    }

    private fun sembrarDatosSiVacio() {
        // Cada siembra es independiente: si una falla (sin conexión, sin
        // permisos...) las demás igualmente se intentan, y los calculadores
        // usarán sus tablas locales de fallback mientras tanto.
        viewModelScope.launch {
            try { repository.sembrarReglasTarifaSiVacio() } catch (e: Exception) { /* fallback local */ }
        }
        viewModelScope.launch {
            try { repository.sembrarDesplazamientosSiVacio() } catch (e: Exception) { /* fallback local */ }
        }
        viewModelScope.launch {
            try { repository.sembrarDietasSiVacio() } catch (e: Exception) { /* fallback local */ }
        }
    }

    private fun escucharPerfilUsuario() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection("usuarios").document(uid)
            .addSnapshotListener { doc, e ->
                if (e == null && doc != null && doc.exists()) {
                    _userRol.value = doc.getString("rol") ?: "Oficial de Mesa"
                    _autorizado3Vistas.value = doc.getBoolean("autorizado3Vistas") ?: false
                }
            }
    }

    fun guardarPartido(partido: Partido) {
        viewModelScope.launch {
            try {
                val total = TarifaCalculator.calcularTotal(
                    partido,
                    DataConstants.listaCategoriasFijas,
                    reglasTarifa.value,
                    reglasDesplazamiento.value,
                    reglasDietas.value
                )
                repository.guardarPartido(partido.copy(totalPartido = total))
            } catch (e: Exception) {
                _errores.emit("Fallo al guardar designación")
            }
        }
    }

    fun eliminarPartido(id: String) {
        viewModelScope.launch {
            try {
                repository.eliminarPartido(id)
            } catch (e: Exception) {
                _errores.emit("Error al eliminar el partido")
            }
        }
    }

    fun guardarSancion(sancion: Sancion) {
        viewModelScope.launch {
            try {
                repository.guardarSancion(sancion)
            } catch (e: Exception) {
                _errores.emit("No tienes permisos en Firebase para Sanciones")
            }
        }
    }

    fun eliminarSancion(id: String) {
        viewModelScope.launch {
            try {
                repository.eliminarSancion(id)
            } catch (e: Exception) {
                _errores.emit("Error al eliminar la sanción")
            }
        }
    }
}