package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.CategoriaConfig
import com.example.basketmesaapp.model.Partido

object TarifaCalculator {

    fun calcularTotal(partido: Partido, categorias: List<CategoriaConfig>): Double {
        val tarifaBase: Double

        fun normalize(s: String) = s.lowercase()
            .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
            .replace("masculino", "masculin").replace("masculina", "masculin")
            .replace("femenino", "femenin").replace("femenina", "femenin")
            .replace(" ", "").replace("/", "").replace("-", "")

        val normalizedPartidoCat = normalize(partido.categoriaId)

        if (partido.rol == "Árbitro") {
            when {
                normalizedPartidoCat.contains("1ªdivision") || normalizedPartidoCat.contains("1ªdivisión") -> tarifaBase = 91.0
                (normalizedPartidoCat.contains("2ªdivision") || normalizedPartidoCat.contains("2ªdivisión")) && normalizedPartidoCat.contains("femenin") -> tarifaBase = 56.0
                (normalizedPartidoCat.contains("2ªdivision") || normalizedPartidoCat.contains("2ªdivisión")) && normalizedPartidoCat.contains("masculin") -> tarifaBase = 42.0
                normalizedPartidoCat.contains("senior") && normalizedPartidoCat.contains("1ª") -> tarifaBase = if (partido.numeroOficiales == 1) 58.60 else 29.30
                normalizedPartidoCat.contains("senior") && normalizedPartidoCat.contains("2ª") -> tarifaBase = if (partido.numeroOficiales == 1) 46.50 else 23.25
                normalizedPartidoCat.contains("junior") && normalizedPartidoCat.contains("1ª") -> tarifaBase = if (partido.numeroOficiales == 1) 44.70 else 22.35
                normalizedPartidoCat.contains("junior") && normalizedPartidoCat.contains("2ª") -> tarifaBase = if (partido.numeroOficiales == 1) 36.0 else 18.0
                normalizedPartidoCat.contains("cadete") && normalizedPartidoCat.contains("1ª") -> tarifaBase = if (partido.numeroOficiales == 1) 24.65 else 16.45
                normalizedPartidoCat.contains("veteran") -> tarifaBase = if (partido.numeroOficiales == 1) 32.90 else 16.45
                normalizedPartidoCat.contains("copanavarra") -> tarifaBase = 43.85
                normalizedPartidoCat.contains("seleccion") -> tarifaBase = 10.0
                else -> {
                    val config = categorias.find {
                        val normalizedConfigId = normalize(it.id)
                        normalizedConfigId == normalizedPartidoCat || normalizedPartidoCat.contains(normalizedConfigId) || normalizedConfigId.contains(normalizedPartidoCat)
                    }
                    tarifaBase = config?.tarifaOficial ?: 0.0
                }
            }
        } else {
            when {
                normalizedPartidoCat.contains("seleccionnavarra") -> {
                    tarifaBase = when {
                        normalizedPartidoCat.contains("junior") -> 25.0
                        normalizedPartidoCat.contains("cadete") -> 17.60
                        normalizedPartidoCat.contains("infantil") -> 17.60
                        normalizedPartidoCat.contains("mini") -> 13.40
                        else -> 0.0
                    }
                }
                normalizedPartidoCat.contains("lfchallenge") -> {
                    tarifaBase = if (partido.numeroOficiales == 4) 48.0 else 64.0
                }
                normalizedPartidoCat.contains("ligaeba") -> {
                    tarifaBase = if (partido.numeroOficiales == 4) 29.12 else 38.83
                }
                normalizedPartidoCat.contains("copanavarra") -> {
                    tarifaBase = if (partido.numeroOficiales == 3) 16.65 else 24.95
                }
                normalizedPartidoCat.contains("2ªdivisionfemenin") -> {
                    tarifaBase = if (partido.numeroOficiales == 1) {
                        if (partido.autorizado3Vistas) 31.60 * 2 else 47.40
                    } else 31.60
                }
                normalizedPartidoCat.contains("2ªdivisionmasculin") -> {
                    tarifaBase = if (partido.numeroOficiales == 1) {
                        if (partido.autorizado3Vistas) 25.0 * 2 else 37.50
                    } else 25.0
                }
                normalizedPartidoCat.contains("senior") && normalizedPartidoCat.contains("1ª") -> {
                    tarifaBase = if (partido.numeroOficiales == 1) {
                        if (partido.autorizado3Vistas) 19.70 * 2 else 29.55
                    } else 19.70
                }
                normalizedPartidoCat.contains("junior") && normalizedPartidoCat.contains("1ª") -> {
                    tarifaBase = if (partido.numeroOficiales == 1) {
                        if (partido.autorizado3Vistas) 17.0 * 2 else 25.50
                    } else 17.0
                }
                else -> {
                    val config = categorias.find {
                        val normalizedConfigId = normalize(it.id)
                        normalizedConfigId == normalizedPartidoCat || normalizedPartidoCat.contains(normalizedConfigId) || normalizedConfigId.contains(normalizedPartidoCat)
                    }
                    tarifaBase = config?.tarifaOficial ?: 0.0
                }
            }
        }

        var total = tarifaBase

        if (partido.cobraDieta) {
            val dietaMonto = when {
                normalizedPartidoCat.contains("seleccion") -> 0.0
                normalizedPartidoCat.contains("senior") || normalizedPartidoCat.contains("2ªdivisionmas") -> 14.0
                normalizedPartidoCat.contains("junior") -> 10.0
                normalizedPartidoCat.contains("cadete") -> 5.0
                else -> 0.0
            }
            total += dietaMonto
        }

        total += if (partido.tipoDesplazamiento != "Ninguno") {
            val match = DataConstants.preciosDesplazamiento.entries.find { partido.polideportivo.contains(it.key, ignoreCase = true) }
            if (match != null) {
                if (partido.tipoDesplazamiento == "Conductor") match.value.first else match.value.second
            } else {
                partido.plusDesplazamiento
            }
        } else {
            partido.plusDesplazamiento
        }

        return total
    }
}