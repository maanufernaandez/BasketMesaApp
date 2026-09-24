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

    // Selección Navarra (cualquier subcategoría, cualquier rol) usa tarifa
    // manual desde que se añadió el paso de "Tarifa" al flujo de creación;
    // ya no tiene tarifas fijas automáticas por subcategoría.

    @Test
    fun `seleccion navarra junior usa la tarifa manual`() {
        val partido = partidoOficial("Selección Navarra Junior").copy(tarifaManual = 25.0)
        val total = TarifaCalculator.calcularTotal(partido, emptyList())
        assertEquals(25.0, total, 0.001)
    }

    @Test
    fun `seleccion navarra cadete usa la tarifa manual`() {
        val partido = partidoOficial("Selección Navarra Cadete").copy(tarifaManual = 17.60)
        val total = TarifaCalculator.calcularTotal(partido, emptyList())
        assertEquals(17.60, total, 0.001)
    }

    @Test
    fun `seleccion navarra infantil usa la tarifa manual`() {
        val partido = partidoOficial("Selección Navarra Infantil").copy(tarifaManual = 17.60)
        val total = TarifaCalculator.calcularTotal(partido, emptyList())
        assertEquals(17.60, total, 0.001)
    }

    @Test
    fun `seleccion navarra mini usa la tarifa manual`() {
        val partido = partidoOficial("Selección Navarra Mini").copy(tarifaManual = 13.40)
        val total = TarifaCalculator.calcularTotal(partido, emptyList())
        assertEquals(13.40, total, 0.001)
    }

    @Test
    fun `seleccion navarra sin tarifa manual informada devuelve 0`() {
        // Si no se ha escrito ninguna tarifa (tarifaManual por defecto = 0.0),
        // el total es 0, tenga o no una subcategoría reconocida.
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
    fun `liga eba con 4 oficiales devuelve 29_12`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Liga Eba", numeroOficiales = 4), emptyList())
        assertEquals(29.12, total, 0.001)
    }

    @Test
    fun `liga eba con otro numero de oficiales devuelve 38_83`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Liga Eba", numeroOficiales = 3), emptyList())
        assertEquals(38.83, total, 0.001)
    }

    @Test
    fun `1a division devuelve 40`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("1ª División Femenina", numeroOficiales = 2), emptyList())
        assertEquals(40.0, total, 0.001)
    }

    @Test
    fun `copa navarra con 3 oficiales devuelve 16_95`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Copa Navarra Femenina", numeroOficiales = 3), emptyList())
        assertEquals(16.95, total, 0.001)
    }

    @Test
    fun `copa navarra con otro numero de oficiales devuelve 25_45`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Copa Navarra Femenina", numeroOficiales = 1), emptyList())
        assertEquals(25.45, total, 0.001)
    }

    @Test
    fun `2a division femenina con varios oficiales devuelve 32`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("2ª División Femenina", numeroOficiales = 3), emptyList())
        assertEquals(32.0, total, 0.001)
    }

    @Test
    fun `2a division femenina en solitario sin autorizacion devuelve 64`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("2ª División Femenina", numeroOficiales = 1, autorizado3Vistas = false),
            emptyList()
        )
        assertEquals(64.0, total, 0.001)
    }

    @Test
    fun `2a division femenina en solitario con autorizacion tambien devuelve 64`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("2ª División Femenina", numeroOficiales = 1, autorizado3Vistas = true),
            emptyList()
        )
        assertEquals(64.0, total, 0.001)
    }

    @Test
    fun `2a division masculina en solitario sin autorizacion devuelve 51`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("2ª División Masculina", numeroOficiales = 1, autorizado3Vistas = false),
            emptyList()
        )
        assertEquals(51.0, total, 0.001)
    }

    @Test
    fun `2a division masculina en solitario con autorizacion tambien devuelve 51`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("2ª División Masculina", numeroOficiales = 1, autorizado3Vistas = true),
            emptyList()
        )
        assertEquals(51.0, total, 0.001)
    }

    @Test
    fun `senior 1a en solitario con autorizacion devuelve 40_20`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("Senior Masculino 1ª", numeroOficiales = 1, autorizado3Vistas = true),
            emptyList()
        )
        assertEquals(40.20, total, 0.001)
    }

    @Test
    fun `senior 1a en solitario sin autorizacion tambien devuelve 40_20`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("Senior Masculino 1ª", numeroOficiales = 1, autorizado3Vistas = false),
            emptyList()
        )
        assertEquals(40.20, total, 0.001)
    }

    @Test
    fun `senior 1a con varios oficiales devuelve 20_10`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Senior Masculino 1ª", numeroOficiales = 3), emptyList())
        assertEquals(20.10, total, 0.001)
    }

    @Test
    fun `senior 2a en solitario devuelve 19_55 y no depende de la autorizacion`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("Senior Masculino 2ª", numeroOficiales = 1, autorizado3Vistas = false),
            emptyList()
        )
        assertEquals(19.55, total, 0.001)
    }

    @Test
    fun `senior 2a con varios oficiales devuelve 12_40`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Senior Masculino 2ª", numeroOficiales = 2), emptyList())
        assertEquals(12.40, total, 0.001)
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
    fun `junior 1a en solitario sin autorizacion devuelve 25_50`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("Junior Masculino 1ª", numeroOficiales = 1, autorizado3Vistas = false),
            emptyList()
        )
        assertEquals(25.50, total, 0.001)
    }

    @Test
    fun `junior 1a con varios oficiales devuelve 17`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Junior Masculino 1ª", numeroOficiales = 3), emptyList())
        assertEquals(17.0, total, 0.001)
    }

    @Test
    fun `junior 2a en solitario devuelve 16_20 y no depende de la autorizacion`() {
        val total = TarifaCalculator.calcularTotal(
            partidoOficial("Junior Masculino 2ª", numeroOficiales = 1, autorizado3Vistas = false),
            emptyList()
        )
        assertEquals(16.20, total, 0.001)
    }

    @Test
    fun `junior 2a con varios oficiales devuelve 11_45`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Junior Masculino 2ª", numeroOficiales = 2), emptyList())
        assertEquals(11.45, total, 0.001)
    }

    @Test
    fun `cadete 1a siempre devuelve 11_55`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Cadete Masculino 1ª", numeroOficiales = 1), emptyList())
        assertEquals(11.55, total, 0.001)
    }

    @Test
    fun `veteranos siempre devuelve 13_60`() {
        val total = TarifaCalculator.calcularTotal(partidoOficial("Torneo Veteranos", numeroOficiales = 1), emptyList())
        assertEquals(13.60, total, 0.001)
    }

    @Test
    fun `categoria no contemplada cae al fallback de configuracion`() {
        // "Exhibición Especial" no aparece en ninguna condición de
        // TarifaDefinitions.OFICIAL_MESA, así que debe resolverse por la
        // configuración de categorías (tercer nivel de fallback).
        val categorias = listOf(
            com.example.basketmesaapp.model.CategoriaConfig("Exhibición Especial", 9.0, 0.0)
        )
        val total = TarifaCalculator.calcularTotal(partidoOficial("Exhibición Especial"), categorias)
        assertEquals(9.0, total, 0.001)
    }
}