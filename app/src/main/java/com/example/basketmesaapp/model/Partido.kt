package com.example.basketmesaapp.model

data class Partido(
    var id: String = "",
    val fecha: String = "",
    val hora: String = "",
    val polideportivo: String = "",
    val categoriaId: String = "",
    val equipoLocal: String = "",
    val equipoVisitante: String = "",
    val numeroOficiales: Int = 3,
    val voySolo: Boolean = false,
    val cobraDieta: Boolean = false,
    val fueraPamplona: Boolean = false,
    val tipoDesplazamiento: String = "Ninguno",
    val plusDesplazamiento: Double = 0.0,
    val totalPartido: Double = 0.0,
    val userId: String = "",
    val rol: String = "Oficial de Mesa",
    var autorizado3Vistas: Boolean = false // NUEVO
)