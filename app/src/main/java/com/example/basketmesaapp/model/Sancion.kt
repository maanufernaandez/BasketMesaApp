package com.example.basketmesaapp.model

data class Sancion(
    var id: String = "",
    var fecha: String = "",
    var motivo: String = "",
    var importe: Double = 0.0,
    var userId: String = ""
)