package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.CategoriaConfig
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.TarifaReglaRemota
import com.example.basketmesaapp.model.DesplazamientoRemoto
import com.example.basketmesaapp.model.DietaRemota

object TarifaCalculator {

    fun calcularTotal(
        partido: Partido,
        categorias: List<CategoriaConfig>,
        reglasRemotas: List<TarifaReglaRemota> = emptyList(),
        reglasDesplazamiento: List<DesplazamientoRemoto> = emptyList(),
        reglasDietas: List<DietaRemota> = emptyList()
    ): Double {
        // Los amistosos ignoran cualquier regla y usan los valores manuales
        // tanto para la tarifa como para el desplazamiento.
        if (partido.isAmistoso) {
            return partido.tarifaManual + partido.plusDesplazamiento
        }

        val categoriaNormalizada = partido.categoriaId.normalizeCategory()
        val esArbitro = partido.rol == "Árbitro"

        // Selección Navarra usa una tarifa manual (varía según la convocatoria),
        // pero la dieta y el desplazamiento se siguen calculando de forma
        // automática igual que en cualquier otra categoría.
        val esSeleccionNavarra = partido.categoriaId.startsWith("Selección Navarra", ignoreCase = true)

        val tarifaBase = if (esSeleccionNavarra) {
            partido.tarifaManual
        } else {
            val reglasLocales = if (esArbitro) TarifaRulesArbitro.reglas else TarifaRulesOficialMesa.reglas
            TarifaReglaRemotaEvaluator.aplicar(reglasRemotas, categoriaNormalizada, partido, partido.rol)
                ?: TarifaReglaRemotaEvaluator.aplicar(reglasLocales, categoriaNormalizada, partido, partido.rol)
                ?: buscarTarifaEnConfig(categoriaNormalizada, categorias)
        }

        val dieta = DietaCalculator.calcular(categoriaNormalizada, partido.cobraDieta, reglasDietas)
        val desplazamiento = DesplazamientoCalculator.calcular(partido, reglasDesplazamiento)

        return tarifaBase + dieta + desplazamiento
    }

    /**
     * Fallback cuando ninguna regla explícita (remota ni local) coincide:
     * busca la tarifa en la configuración de categorías, comparando IDs
     * normalizados de forma flexible (igualdad o contención en cualquier
     * sentido, igual que hacía el `when` original).
     */
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