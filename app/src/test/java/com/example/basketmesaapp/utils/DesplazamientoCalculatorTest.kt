package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import org.junit.Assert.assertEquals
import org.junit.Test

class DesplazamientoCalculatorTest {

    @Test
    fun `sin desplazamiento devuelve el plus manual`() {
        val partido = Partido(tipoDesplazamiento = "Ninguno", plusDesplazamiento = 7.5)
        assertEquals(7.5, DesplazamientoCalculator.calcular(partido), 0.001)
    }

    @Test
    fun `conductor con polideportivo conocido devuelve el precio de conductor`() {
        val partido = Partido(
            tipoDesplazamiento = "Conductor",
            polideportivo = "Pabellón de Tudela",
            plusDesplazamiento = 0.0
        )
        assertEquals(65.80, DesplazamientoCalculator.calcular(partido), 0.001)
    }

    @Test
    fun `acompanante con polideportivo conocido devuelve el precio de acompanante`() {
        val partido = Partido(
            tipoDesplazamiento = "Acompañante",
            polideportivo = "Pabellón de Tudela",
            plusDesplazamiento = 0.0
        )
        assertEquals(15.04, DesplazamientoCalculator.calcular(partido), 0.001)
    }

    @Test
    fun `polideportivo desconocido cae al plus manual`() {
        val partido = Partido(
            tipoDesplazamiento = "Conductor",
            polideportivo = "Pabellón Inexistente",
            plusDesplazamiento = 12.0
        )
        assertEquals(12.0, DesplazamientoCalculator.calcular(partido), 0.001)
    }

    @Test
    fun `la busqueda de polideportivo ignora mayusculas`() {
        val partido = Partido(
            tipoDesplazamiento = "Conductor",
            polideportivo = "polideportivo tudela norte",
            plusDesplazamiento = 0.0
        )
        assertEquals(65.80, DesplazamientoCalculator.calcular(partido), 0.001)
    }
}