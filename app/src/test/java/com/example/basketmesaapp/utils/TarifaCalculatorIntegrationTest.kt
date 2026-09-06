package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifica el comportamiento de [TarifaCalculator.calcularTotal] como un
 * todo: el caso especial de partido amistoso, y la suma correcta de
 * tarifa base + dieta + desplazamiento para un caso realista.
 */
class TarifaCalculatorIntegrationTest {

    @Test
    fun `partido amistoso ignora cualquier regla y usa los valores manuales`() {
        val partido = Partido(
            isAmistoso = true,
            tarifaManual = 20.0,
            plusDesplazamiento = 5.0,
            // Estos campos deberían ser ignorados al ser amistoso:
            categoriaId = "1ª División Femenina",
            rol = "Árbitro",
            cobraDieta = true
        )
        val total = TarifaCalculator.calcularTotal(partido, emptyList())
        assertEquals(25.0, total, 0.001)
    }

    @Test
    fun `total combina tarifa base, dieta y desplazamiento`() {
        // Árbitro de Senior 1ª en solitario (58.60) + dieta de senior (no aplica
        // a árbitros según la tabla, la dieta es independiente del rol) +
        // desplazamiento fijo por polideportivo conocido.
        val partido = Partido(
            categoriaId = "Senior Masculino 1ª",
            rol = "Árbitro",
            numeroOficiales = 1,
            cobraDieta = true,
            tipoDesplazamiento = "Conductor",
            polideportivo = "Pabellón de Tafalla"
        )
        // tarifaBase = 58.60, dieta = 14.0 (contains "senior"), desplazamiento = 20.0
        val total = TarifaCalculator.calcularTotal(partido, emptyList())
        assertEquals(58.60 + 14.0 + 20.0, total, 0.001)
    }

    @Test
    fun `total sin dieta ni desplazamiento especial usa el plus manual`() {
        val partido = Partido(
            categoriaId = "Copa Navarra Masculina",
            rol = "Oficial de Mesa",
            numeroOficiales = 4,
            cobraDieta = false,
            tipoDesplazamiento = "Ninguno",
            plusDesplazamiento = 3.0
        )
        // tarifaBase = 25.45 (copanavarra, numeroOficiales != 3), dieta = 0.0, desplazamiento = 3.0
        val total = TarifaCalculator.calcularTotal(partido, emptyList())
        assertEquals(25.45 + 3.0, total, 0.001)
    }
}