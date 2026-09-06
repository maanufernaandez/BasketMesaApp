package com.example.basketmesaapp.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class DietaCalculatorTest {

    @Test
    fun `sin cobrarDieta siempre devuelve 0`() {
        assertEquals(0.0, DietaCalculator.calcular("senior1ª", cobraDieta = false), 0.001)
    }

    @Test
    fun `seleccion no cobra dieta aunque cobraDieta sea true`() {
        assertEquals(0.0, DietaCalculator.calcular("seleccionnavarra", cobraDieta = true), 0.001)
    }

    @Test
    fun `senior cobra 14 de dieta`() {
        assertEquals(14.0, DietaCalculator.calcular("seniormasculino1ª", cobraDieta = true), 0.001)
    }

    @Test
    fun `2a division masculina cobra 14 de dieta`() {
        assertEquals(14.0, DietaCalculator.calcular("2ªdivisionmasculin", cobraDieta = true), 0.001)
    }

    @Test
    fun `junior cobra 10 de dieta`() {
        assertEquals(10.0, DietaCalculator.calcular("juniormasculino1ª", cobraDieta = true), 0.001)
    }

    @Test
    fun `cadete cobra 5 de dieta`() {
        assertEquals(5.0, DietaCalculator.calcular("cadetemasculino1ª", cobraDieta = true), 0.001)
    }

    @Test
    fun `categoria sin regla de dieta devuelve 0`() {
        assertEquals(0.0, DietaCalculator.calcular("copanavarra", cobraDieta = true), 0.001)
    }
}