package com.example.basketmesaapp.model

data class DesplazamientoRemoto(
    var id: String = "",
    val localidad: String = "",
    val conductor: Double = 0.0,
    val acompanante: Double = 0.0
)

data class DietaRemota(
    var id: String = "",
    val categoria: String = "",
    val importe: Double = 0.0
)

data class EquipoRemoto(
    var id: String = "",
    val categoria: String = "",
    val nombre: String = "",
    val polideportivos: List<String> = emptyList()
)

data class FestivoRemoto(
    var id: String = "",
    val fecha: String = ""
)