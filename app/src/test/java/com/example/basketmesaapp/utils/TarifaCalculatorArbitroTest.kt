package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Cubre todas las reglas de [TarifaRulesArbitro] a través de la API pública
 * [TarifaCalculator.calcularTotal], usando categoriaId "reales" (con acentos,
 * espacios y mayúsculas) para verificar también [normalizeCategory].
 */
class TarifaCalculatorArbitroTest {

    private fun partidoArbitro(
        categoriaId: String,
        numeroOficiales: Int = 3
    ) = Partido(
        categoriaId = categoriaId,
        rol = "Árbitro",
        numeroOficiales = numeroOficiales
    )

    @Test
    fun `1a division devuelve 94`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("1ª División Femenina"), emptyList())
        assertEquals(94.0, total, 0.001)
    }

    @Test
    fun `2a division femenina devuelve 58`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("2ª División Femenina"), emptyList())
        assertEquals(58.0, total, 0.001)
    }

    @Test
    fun `2a division masculina devuelve 42_85`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("2ª División Masculina"), emptyList())
        assertEquals(42.85, total, 0.001)
    }

    @Test
    fun `senior 1a con un oficial devuelve 59_80`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Senior Masculino 1ª", numeroOficiales = 1), emptyList())
        assertEquals(59.80, total, 0.001)
    }

    @Test
    fun `senior 1a con varios oficiales devuelve 29_90`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Senior Masculino 1ª", numeroOficiales = 2), emptyList())
        assertEquals(29.90, total, 0.001)
    }

    @Test
    fun `senior 2a con un oficial devuelve 47_40`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Senior Femenino 2ª", numeroOficiales = 1), emptyList())
        assertEquals(47.40, total, 0.001)
    }

    @Test
    fun `senior 2a con varios oficiales devuelve 23_70`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Senior Femenino 2ª", numeroOficiales = 3), emptyList())
        assertEquals(23.70, total, 0.001)
    }

    @Test
    fun `junior 1a con un oficial devuelve 45_60`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Junior Masculino 1ª", numeroOficiales = 1), emptyList())
        assertEquals(45.60, total, 0.001)
    }

    @Test
    fun `junior 1a con varios oficiales devuelve 22_80`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Junior Masculino 1ª", numeroOficiales = 2), emptyList())
        assertEquals(22.80, total, 0.001)
    }

    @Test
    fun `junior 2a con un oficial devuelve 36_70`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Junior Femenino 2ª", numeroOficiales = 1), emptyList())
        assertEquals(36.70, total, 0.001)
    }

    @Test
    fun `junior 2a con varios oficiales devuelve 18_35`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Junior Femenino 2ª", numeroOficiales = 3), emptyList())
        assertEquals(18.35, total, 0.001)
    }

    @Test
    fun `cadete 1a con un oficial devuelve 25_20`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Cadete Masculino 1ª", numeroOficiales = 1), emptyList())
        assertEquals(25.20, total, 0.001)
    }

    @Test
    fun `cadete 1a con varios oficiales devuelve 16_80`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Cadete Masculino 1ª", numeroOficiales = 2), emptyList())
        assertEquals(16.80, total, 0.001)
    }

    @Test
    fun `veteranos con un oficial devuelve 33_60`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Torneo Veteranos", numeroOficiales = 1), emptyList())
        assertEquals(33.60, total, 0.001)
    }

    @Test
    fun `veteranos con varios oficiales devuelve 16_80`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Torneo Veteranos", numeroOficiales = 2), emptyList())
        assertEquals(16.80, total, 0.001)
    }

    @Test
    fun `copa navarra devuelve 44_75`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Copa Navarra Femenina"), emptyList())
        assertEquals(44.75, total, 0.001)
    }

    @Test
    fun `seleccion navarra usa la tarifa manual en vez de una tarifa fija`() {
        val partido = partidoArbitro("Selección Navarra").copy(tarifaManual = 15.0)
        val total = TarifaCalculator.calcularTotal(partido, emptyList())
        assertEquals(15.0, total, 0.001)
    }

    @Test
    fun `lf challenge devuelve 270 por arbitro`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("LF Challenge", numeroOficiales = 2), emptyList())
        assertEquals(270.0, total, 0.001)
    }

    @Test
    fun `liga eba devuelve 140 por arbitro`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Liga Eba", numeroOficiales = 2), emptyList())
        assertEquals(140.0, total, 0.001)
    }

    @Test
    fun `categoria no contemplada cae al fallback de configuracion`() {
        // "Exhibición Especial" no aparece en ninguna condición de
        // TarifaDefinitions.ARBITRO, así que debe resolverse por la
        // configuración de categorías (tercer nivel de fallback).
        val categorias = listOf(
            com.example.basketmesaapp.model.CategoriaConfig("Exhibición Especial", 12.0, 0.0)
        )
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Exhibición Especial"), categorias)
        assertEquals(12.0, total, 0.001)
    }

    @Test
    fun `categoria totalmente desconocida sin match en config devuelve 0`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Categoria Inventada"), emptyList())
        assertEquals(0.0, total, 0.001)
    }
}