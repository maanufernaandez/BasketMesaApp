package com.example.basketmesaapp.utils

object DietaCalculator {
    fun calcular(categoriaNormalizada: String, cobraDieta: Boolean): Double {
        if (!cobraDieta) return 0.0
        return when {
            categoriaNormalizada.contains("seleccion") -> 0.0
            categoriaNormalizada.contains("senior") || categoriaNormalizada.contains("2ªdivisionmas") -> 14.0
            categoriaNormalizada.contains("junior") -> 10.0
            categoriaNormalizada.contains("cadete") -> 5.0
            else -> 0.0
        }
    }
}