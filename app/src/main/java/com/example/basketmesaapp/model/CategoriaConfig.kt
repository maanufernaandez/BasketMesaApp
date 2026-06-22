package com.example.basketmesaapp.model

data class CategoriaConfig(
    val id: String = "",
    val tarifaOficial: Double = 0.0,
    val dieta: Double = 0.0
)

data class Equipo(val nombre: String, val polideportivos: List<String>)
