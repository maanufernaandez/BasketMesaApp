package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.DesplazamientoRemoto
import com.example.basketmesaapp.model.Partido

object DesplazamientoCalculator {
    fun calcular(
        partido: Partido,
        reglasRemotas: List<DesplazamientoRemoto> = emptyList()
    ): Double {
        if (partido.tipoDesplazamiento == "Ninguno") return partido.plusDesplazamiento

        val matchRemoto = reglasRemotas.find { partido.polideportivo.contains(it.localidad, ignoreCase = true) }
        if (matchRemoto != null) {
            return if (partido.tipoDesplazamiento == "Conductor") matchRemoto.conductor else matchRemoto.acompanante
        }

        val matchLocal = DataConstants.preciosDesplazamiento.entries.find {
            partido.polideportivo.contains(it.key, ignoreCase = true)
        } ?: return partido.plusDesplazamiento

        return if (partido.tipoDesplazamiento == "Conductor") matchLocal.value.first else matchLocal.value.second
    }
}