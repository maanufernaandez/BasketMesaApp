package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportePartidoTest {

    private val base = Partido(
        id = "p1",
        fecha = "2026-10-10",
        hora = "17:00",
        categoriaId = "Senior Masculino 1ª",
        equipoLocal = "A",
        equipoVisitante = "B",
        numeroOficiales = 2,
        polideportivo = "Larrabide",
        totalPartido = 20.10
    )

    @Test
    fun `cambiar solo equipos fecha u hora no cambia el importe`() {
        val editado = base.copy(fecha = "2026-10-11", hora = "18:30", equipoLocal = "C", equipoVisitante = "D")
        assertFalse(ImportePartido.cambiaElImporte(base, editado))
    }

    @Test
    fun `cambiar el numero de oficiales cambia el importe`() {
        assertTrue(ImportePartido.cambiaElImporte(base, base.copy(numeroOficiales = 1)))
    }

    @Test
    fun `cambiar la categoria cambia el importe`() {
        assertTrue(ImportePartido.cambiaElImporte(base, base.copy(categoriaId = "Junior Masculino 1ª")))
    }

    @Test
    fun `cambiar la tarifa manual cambia el importe`() {
        assertTrue(ImportePartido.cambiaElImporte(base, base.copy(tarifaManual = 30.0)))
    }

    @Test
    fun `pasar a cobrar dieta cambia el importe`() {
        assertTrue(ImportePartido.cambiaElImporte(base, base.copy(cobraDieta = true)))
    }

    @Test
    fun `cambiar el desplazamiento cambia el importe`() {
        assertTrue(ImportePartido.cambiaElImporte(base, base.copy(tipoDesplazamiento = "Conductor")))
        assertTrue(ImportePartido.cambiaElImporte(base, base.copy(polideportivo = "Tudela")))
        assertTrue(ImportePartido.cambiaElImporte(base, base.copy(plusDesplazamiento = 5.0)))
    }
}