package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido

object DesplazamientoCalculator {
    fun calcular(partido: Partido): Double {
        if (partido.tipoDesplazamiento == "Ninguno") return partido.plusDesplazamiento

        val match = DataConstants.preciosDesplazamiento.entries.find {
            partido.polideportivo.contains(it.key, ignoreCase = true)
        } ?: return partido.plusDesplazamiento

        return if (partido.tipoDesplazamiento == "Conductor") match.value.first else match.value.second
    }
}