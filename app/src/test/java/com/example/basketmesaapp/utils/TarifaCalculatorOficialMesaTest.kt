package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Cubre todas las reglas de [TarifaRulesOficialMesa], incluyendo el bloque de
 * Selección Navarra (junior/cadete/infantil/mini/otros) y las variantes de
 * "3 funciones vistas" que duplican la tarifa cuando aplica.
 */
class TarifaCalculatorOficialMesaTest {

    private fun partidoOficial(
        categoriaId: String,
        numeroOficiales: Int = 3,
        autorizado3Vistas: Boolean = false
    ) = Partido(
        categoriaId = categoriaId,
        rol = "Oficial de Mesa",
        numeroOficiales = numeroOficiales,
        autorizado3Vistas = autorizado3Vistas
    )

    @Test
    fun `seleccion navarra junior devuelve 25`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Selección Navarra Junior"), emptyList())
        assertEquals(25.0, total, 0.001)
    }

    @Test
    fun `seleccion navarra cadete devuelve 17,60`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Selección Navarra Cadete"), emptyList())
        assertEquals(17.60, total, 0.001)
    }

    @Test
    fun `seleccion navarra infantil devuelve 17,60`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Selección Navarra Infantil"), emptyList())
        assertEquals(17.60, total, 0.001)
    }

    @Test
    fun `seleccion navarra mini devuelve 13,40`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Selección Navarra Mini"), emptyList())
        assertEquals(13.40, total, 0.001)
    }

    @Test
    fun `seleccion navarra sin subcategoria conocida devuelve 0`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Selección Navarra Absoluta"), emptyList())
        assertEquals(0.0, total, 0.001)
    }

    @Test
    fun `lf challenge con 4 oficiales devuelve 48`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("LF Challenge", numeroOficiales = 4), emptyList())
        assertEquals(48.0, total, 0.001)
    }

    @Test
    fun `lf challenge con otro numero de oficiales devuelve 64`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("LF Challenge", numeroOficiales = 3), emptyList())
        assertEquals(64.0, total, 0.001)
    }

    @Test
    fun `liga eba con 4 oficiales devuelve 29,12`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Liga Eba", numeroOficiales = 4), emptyList())
        assertEquals(29.12, total, 0.001)
    }

    @Test
    fun `liga eba con otro numero de oficiales devuelve 38,83`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Liga Eba", numeroOficiales = 3), emptyList())
        assertEquals(38.83, total, 0.001)
    }

    @Test
    fun `copa navarra con 3 oficiales devuelve 16,65`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Copa Navarra Femenina", numeroOficiales = 3), emptyList())
        assertEquals(16.65, total, 0.001)
    }

    @Test
    fun `copa navarra con otro numero de oficiales devuelve 25,45`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Copa Navarra Femenina", numeroOficiales = 1), emptyList())
        assertEquals(25.45, total, 0.001)
    }

    @Test
    fun `2a division femenina con varios oficiales devuelve 31,60`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("2ª División Femenina", numeroOficiales = 3), emptyList())
        assertEquals(31.60, total, 0.001)
    }

    @Test
    fun `2a division femenina en solitario sin autorizacion devuelve 47,40`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("2ª División Femenina", numeroOficiales = 1, autorizado3Vistas = false),
            emptyList()
        )
        assertEquals(47.40, total, 0.001)
    }

    @Test
    fun `2a division femenina en solitario con autorizacion devuelve el doble`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("2ª División Femenina", numeroOficiales = 1, autorizado3Vistas = true),
            emptyList()
        )
        assertEquals(31.60 * 2, total, 0.001)
    }

    @Test
    fun `2a division masculina en solitario sin autorizacion devuelve 37,50`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("2ª División Masculina", numeroOficiales = 1, autorizado3Vistas = false),
            emptyList()
        )
        assertEquals(37.50, total, 0.001)
    }

    @Test
    fun `2a division masculina en solitario con autorizacion devuelve el doble`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("2ª División Masculina", numeroOficiales = 1, autorizado3Vistas = true),
            emptyList()
        )
        assertEquals(25.0 * 2, total, 0.001)
    }

    @Test
    fun `senior 1a en solitario con autorizacion devuelve el doble`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("Senior Masculino 1ª", numeroOficiales = 1, autorizado3Vistas = true),
            emptyList()
        )
        assertEquals(19.70 * 2, total, 0.001)
    }

    @Test
    fun `senior 1a con varios oficiales devuelve 19,70`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Senior Masculino 1ª", numeroOficiales = 3), emptyList())
        assertEquals(19.70, total, 0.001)
    }

    @Test
    fun `junior 1a en solitario con autorizacion devuelve el doble`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("Junior Masculino 1ª", numeroOficiales = 1, autorizado3Vistas = true),
            emptyList()
        )
        assertEquals(17.0 * 2, total, 0.001)
    }

    @Test
    fun `junior 1a con varios oficiales devuelve 17`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Junior Masculino 1ª", numeroOficiales = 3), emptyList())
        assertEquals(17.0, total, 0.001)
    }

    @Test
    fun `categoria no contemplada cae al fallback de configuracion`() {
        val categorias = listOf(
            com.example.basketmesaapp.model.CategoriaConfig("Torneo Veteranos", 13.35, 0.0)
        )
        val total = TarifaCalculator.calcularTotal(partidoOficial("Torneo Veteranos"), categorias)
        assertEquals(13.35, total, 0.001)
    }
}