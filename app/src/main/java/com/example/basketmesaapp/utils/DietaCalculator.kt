package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.DietaRemota

object DietaCalculator {
    fun calcular(
        categoriaNormalizada: String,
        cobraDieta: Boolean,
        reglasRemotas: List<DietaRemota> = emptyList()
    ): Double {
        if (!cobraDieta) return 0.0
        if (categoriaNormalizada.contains("seleccion")) return 0.0

        val matchRemoto = reglasRemotas.find { categoriaNormalizada.contains(it.categoria) }
        if (matchRemoto != null) return matchRemoto.importe

        val matchLocal = DataConstants.dietasPorCategoria.entries.find { categoriaNormalizada.contains(it.key) }
        return matchLocal?.value ?: 0.0
    }
}