package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.Sancion
import org.junit.Assert.assertEquals
import org.junit.Test

class AgrupacionMensualTest {

    @Test
    fun `agrupa por mes con el mas reciente primero`() {
        val partidos = listOf(
            Partido(id = "1", fecha = "2026-09-20", hora = "10:00", totalPartido = 20.0),
            Partido(id = "2", fecha = "2026-10-05", hora = "12:00", totalPartido = 30.0),
            Partido(id = "3", fecha = "2026-10-05", hora = "18:00", totalPartido = 10.0)
        )
        val meses = AgrupacionMensual.agrupar(partidos, emptyList())

        assertEquals(listOf("2026-10", "2026-09"), meses.map { it.clave })
        // Dentro de un mismo día, la hora más tardía va primero
        assertEquals(listOf("3", "2"), meses[0].partidos.map { it.id })
    }

    @Test
    fun `el total neto resta las sanciones del mes`() {
        val partidos = listOf(Partido(id = "1", fecha = "2026-10-05", totalPartido = 50.0))
        val sanciones = listOf(Sancion(id = "s1", fecha = "2026-10-07", importe = 12.5))
        val mes = AgrupacionMensual.agrupar(partidos, sanciones).single()

        assertEquals(12.5, mes.totalSanciones, 0.001)
        assertEquals(37.5, mes.totalNeto, 0.001)
    }

    @Test
    fun `un mes con solo sanciones tambien aparece`() {
        val mes = AgrupacionMensual.agrupar(emptyList(), listOf(Sancion(id = "s1", fecha = "2026-11-01", importe = 5.0))).single()
        assertEquals("2026-11", mes.clave)
        assertEquals(-5.0, mes.totalNeto, 0.001)
    }

    @Test
    fun `el nombre del mes sale en espanol con mayuscula inicial`() {
        assertEquals("Octubre 2026", AgrupacionMensual.nombreDeMes("2026-10"))
    }

    @Test
    fun `una fecha invalida cae en Mes Desconocido`() {
        assertEquals("Mes Desconocido", AgrupacionMensual.nombreDeMes(AgrupacionMensual.claveDeMes("")))
    }
}