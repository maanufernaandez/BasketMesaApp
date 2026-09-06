package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.CategoriaConfig
import com.example.basketmesaapp.model.Partido

object TarifaCalculator {

    fun calcularTotal(partido: Partido, categorias: List<CategoriaConfig>): Double {
        // Los amistosos ignoran cualquier regla y usan los valores manuales.
        if (partido.isAmistoso) {
            return partido.tarifaManual + partido.plusDesplazamiento
        }

        val categoriaNormalizada = partido.categoriaId.normalizeCategory()
        val esArbitro = partido.rol == "Árbitro"
        val reglas = if (esArbitro) TarifaRulesArbitro.reglas else TarifaRulesOficialMesa.reglas

        val tarifaBase = reglas.aplicar(categoriaNormalizada, partido)
            ?: buscarTarifaEnConfig(categoriaNormalizada, categorias)

        val dieta = DietaCalculator.calcular(categoriaNormalizada, partido.cobraDieta)
        val desplazamiento = DesplazamientoCalculator.calcular(partido)

        return tarifaBase + dieta + desplazamiento
    }

    private fun buscarTarifaEnConfig(categoriaNormalizada: String, categorias: List<CategoriaConfig>): Double {
        val config = categorias.find {
            val normalizedConfigId = it.id.normalizeCategory()
            normalizedConfigId == categoriaNormalizada ||
                    categoriaNormalizada.contains(normalizedConfigId) ||
                    normalizedConfigId.contains(categoriaNormalizada)
        }
        return config?.tarifaOficial ?: 0.0
    }
}