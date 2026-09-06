package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import org.junit.Assert.assertEquals
import org.junit.Test

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
    fun `1a division devuelve 91`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("1ª División Femenina"), emptyList())
        assertEquals(91.0, total, 0.001)
    }

    @Test
    fun `2a division femenina devuelve 56`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("2ª División Femenina"), emptyList())
        assertEquals(56.0, total, 0.001)
    }

    @Test
    fun `2a division masculina devuelve 42`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("2ª División Masculina"), emptyList())
        assertEquals(42.0, total, 0.001)
    }

    @Test
    fun `senior 1a con un oficial devuelve 58,60`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Senior Masculino 1ª", numeroOficiales = 1), emptyList())
        assertEquals(58.60, total, 0.001)
    }

    @Test
    fun `senior 1a con varios oficiales devuelve 29,30`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Senior Masculino 1ª", numeroOficiales = 2), emptyList())
        assertEquals(29.30, total, 0.001)
    }

    @Test
    fun `senior 2a con un oficial devuelve 46,50`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Senior Femenino 2ª", numeroOficiales = 1), emptyList())
        assertEquals(46.50, total, 0.001)
    }

    @Test
    fun `senior 2a con varios oficiales devuelve 23,25`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Senior Femenino 2ª", numeroOficiales = 3), emptyList())
        assertEquals(23.25, total, 0.001)
    }

    @Test
    fun `junior 1a con un oficial devuelve 44,70`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Junior Masculino 1ª", numeroOficiales = 1), emptyList())
        assertEquals(44.70, total, 0.001)
    }

    @Test
    fun `junior 1a con varios oficiales devuelve 22,35`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Junior Masculino 1ª", numeroOficiales = 2), emptyList())
        assertEquals(22.35, total, 0.001)
    }

    @Test
    fun `junior 2a con un oficial devuelve 36`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Junior Femenino 2ª", numeroOficiales = 1), emptyList())
        assertEquals(36.0, total, 0.001)
    }

    @Test
    fun `junior 2a con varios oficiales devuelve 18`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Junior Femenino 2ª", numeroOficiales = 3), emptyList())
        assertEquals(18.0, total, 0.001)
    }

    @Test
    fun `cadete 1a con un oficial devuelve 24,65`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Cadete Masculino 1ª", numeroOficiales = 1), emptyList())
        assertEquals(24.65, total, 0.001)
    }

    @Test
    fun `cadete 1a con varios oficiales devuelve 16,45`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Cadete Masculino 1ª", numeroOficiales = 2), emptyList())
        assertEquals(16.45, total, 0.001)
    }

    @Test
    fun `veteranos con un oficial devuelve 32,90`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Torneo Veteranos", numeroOficiales = 1), emptyList())
        assertEquals(32.90, total, 0.001)
    }

    @Test
    fun `veteranos con varios oficiales devuelve 16,45`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Torneo Veteranos", numeroOficiales = 2), emptyList())
        assertEquals(16.45, total, 0.001)
    }

    @Test
    fun `copa navarra devuelve 43,85`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Copa Navarra Femenina"), emptyList())
        assertEquals(43.85, total, 0.001)
    }

    @Test
    fun `seleccion devuelve 10`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Selección Navarra"), emptyList())
        assertEquals(10.0, total, 0.001)
    }

    @Test
    fun `categoria no contemplada cae al fallback de configuracion`() {
        val categorias = listOf(
            com.example.basketmesaapp.model.CategoriaConfig("LF Challenge", 64.0, 0.0)
        )
        val total = TarifaCalculator.calcularTotal(partidoArbitro("LF Challenge"), categorias)
        assertEquals(64.0, total, 0.001)
    }

    @Test
    fun `categoria totalmente desconocida sin match en config devuelve 0`() {
        val total = TarifaCalculator.calcularTotal(partidoArbitro("Categoria Inventada"), emptyList())
        assertEquals(0.0, total, 0.001)
    }
}