package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.CategoriaConfig
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.TarifaReglaRemota
import com.example.basketmesaapp.model.DesplazamientoRemoto
import com.example.basketmesaapp.model.DietaRemota

/**
 * Orquesta el cálculo del importe total de un partido.
 *
 * Prioridad de resolución de la tarifa base:
 *  1. [reglasRemotas] cargadas desde Firestore (colección `tarifas_reglas`),
 *     si hay alguna que coincida con la categoría del partido.
 *  2. Tablas locales [TarifaRulesArbitro] / [TarifaRulesOficialMesa], como
 *     fallback si Firestore aún no tiene datos, la lectura falla, o no hay
 *     conexión.
 *  3. [CategoriaConfig] cargada aparte, como último recurso (igual que en
 *     la versión anterior de este calculador).
 *
 * Este diseño permite editar tarifas sin publicar una nueva versión de la
 * app, sin arriesgar que la app se rompa si Firestore no responde.
 */
object TarifaCalculator {

    fun calcularTotal(
        partido: Partido,
        categorias: List<CategoriaConfig>,
        reglasRemotas: List<TarifaReglaRemota> = emptyList(),
        reglasDesplazamiento: List<DesplazamientoRemoto> = emptyList(),
        reglasDietas: List<DietaRemota> = emptyList()
    ): Double {
        // Los amistosos ignoran cualquier regla y usan los valores manuales.
        if (partido.isAmistoso) {
            return partido.tarifaManual + partido.plusDesplazamiento
        }

        val categoriaNormalizada = partido.categoriaId.normalizeCategory()
        val esArbitro = partido.rol == "Árbitro"
        val reglasLocales = if (esArbitro) TarifaRulesArbitro.reglas else TarifaRulesOficialMesa.reglas

        val tarifaBase = TarifaReglaRemotaEvaluator.aplicar(reglasRemotas, categoriaNormalizada, partido, partido.rol)
            ?: TarifaReglaRemotaEvaluator.aplicar(reglasLocales, categoriaNormalizada, partido, partido.rol)
            ?: buscarTarifaEnConfig(categoriaNormalizada, categorias)

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