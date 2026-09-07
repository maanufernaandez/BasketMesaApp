package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.TarifaReglaRemota
import com.example.basketmesaapp.model.TipoCalculoTarifa
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Tests del intérprete puro [TarifaReglaRemotaEvaluator], independientes de
 * Firestore: las reglas se construyen a mano en cada test.
 */
class TarifaReglaRemotaEvaluatorTest {

    private fun partido(numeroOficiales: Int = 3, autorizado3Vistas: Boolean = false) =
        Partido(numeroOficiales = numeroOficiales, autorizado3Vistas = autorizado3Vistas)

    @Test
    fun `sin reglas devuelve null`() {
        val resultado =
            TarifaReglaRemotaEvaluator.aplicar(emptyList(), "1ªdivision", partido(), "Árbitro")
        assertNull(resultado)
    }

    @Test
    fun `regla FIJO devuelve siempre el mismo valor`() {
        val reglas = listOf(
            TarifaReglaRemota(rol = "Árbitro", orden = 0, condiciones = listOf("1ªdivision"), tipoCalculo = TipoCalculoTarifa.FIJO.name, valorPorDefecto = 91.0)
        )
        val resultado =
            TarifaReglaRemotaEvaluator.aplicar(reglas, "1ªdivisionfemenin", partido(), "Árbitro")
        assertEquals(91.0, resultado!!, 0.001)
    }

    @Test
    fun `regla SEGUN_NUMERO_OFICIALES devuelve valorConReferencia si coincide el numero`() {
        val reglas = listOf(
            TarifaReglaRemota(
                rol = "Oficial de Mesa", orden = 0, condiciones = listOf("lfchallenge"),
                tipoCalculo = TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name,
                valorPorDefecto = 64.0, numeroOficialesReferencia = 4, valorConReferencia = 48.0
            )
        )
        val resultado = TarifaReglaRemotaEvaluator.aplicar(
            reglas,
            "lfchallenge",
            partido(numeroOficiales = 4),
            "Oficial de Mesa"
        )
        assertEquals(48.0, resultado!!, 0.001)
    }

    @Test
    fun `regla SEGUN_NUMERO_OFICIALES devuelve valorPorDefecto si no coincide el numero`() {
        val reglas = listOf(
            TarifaReglaRemota(
                rol = "Oficial de Mesa", orden = 0, condiciones = listOf("lfchallenge"),
                tipoCalculo = TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name,
                valorPorDefecto = 64.0, numeroOficialesReferencia = 4, valorConReferencia = 48.0
            )
        )
        val resultado = TarifaReglaRemotaEvaluator.aplicar(
            reglas,
            "lfchallenge",
            partido(numeroOficiales = 3),
            "Oficial de Mesa"
        )
        assertEquals(64.0, resultado!!, 0.001)
    }

    @Test
    fun `regla SOLITARIO_CON_AUTORIZACION sin autorizacion usa valorSolitarioSinAutorizacion`() {
        val reglas = listOf(
            TarifaReglaRemota(
                rol = "Oficial de Mesa", orden = 0, condiciones = listOf("2ªdivisionmasculin"),
                tipoCalculo = TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION.name,
                valorPorDefecto = 25.0, valorConReferencia = 25.0, valorSolitarioSinAutorizacion = 37.50
            )
        )
        val resultado = TarifaReglaRemotaEvaluator.aplicar(
            reglas,
            "2ªdivisionmasculin",
            partido(numeroOficiales = 1, autorizado3Vistas = false),
            "Oficial de Mesa"
        )
        assertEquals(37.50, resultado!!, 0.001)
    }

    @Test
    fun `regla SOLITARIO_CON_AUTORIZACION con autorizacion duplica valorConReferencia`() {
        val reglas = listOf(
            TarifaReglaRemota(
                rol = "Oficial de Mesa", orden = 0, condiciones = listOf("2ªdivisionmasculin"),
                tipoCalculo = TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION.name,
                valorPorDefecto = 25.0, valorConReferencia = 25.0, valorSolitarioSinAutorizacion = 37.50
            )
        )
        val resultado = TarifaReglaRemotaEvaluator.aplicar(
            reglas,
            "2ªdivisionmasculin",
            partido(numeroOficiales = 1, autorizado3Vistas = true),
            "Oficial de Mesa"
        )
        assertEquals(50.0, resultado!!, 0.001)
    }

    @Test
    fun `regla SOLITARIO_CON_AUTORIZACION con varios oficiales usa valorPorDefecto`() {
        val reglas = listOf(
            TarifaReglaRemota(
                rol = "Oficial de Mesa", orden = 0, condiciones = listOf("2ªdivisionmasculin"),
                tipoCalculo = TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION.name,
                valorPorDefecto = 25.0, valorConReferencia = 25.0, valorSolitarioSinAutorizacion = 37.50
            )
        )
        val resultado = TarifaReglaRemotaEvaluator.aplicar(
            reglas, "2ªdivisionmasculin", partido(numeroOficiales = 3), "Oficial de Mesa"
        )
        assertEquals(25.0, resultado!!, 0.001)
    }

    @Test
    fun `las condiciones son AND todas deben cumplirse`() {
        val reglas = listOf(
            TarifaReglaRemota(rol = "Árbitro", orden = 0, condiciones = listOf("senior", "1ª"), tipoCalculo = TipoCalculoTarifa.FIJO.name, valorPorDefecto = 29.30)
        )
        // Solo contiene "senior", no "1ª" -> no debe aplicar
        val resultado =
            TarifaReglaRemotaEvaluator.aplicar(reglas, "seniorfemenin2ª", partido(), "Árbitro")
        assertNull(resultado)
    }

    @Test
    fun `se filtra por rol una regla de otro rol nunca aplica`() {
        val reglas = listOf(
            TarifaReglaRemota(rol = "Oficial de Mesa", orden = 0, condiciones = listOf("1ªdivision"), tipoCalculo = TipoCalculoTarifa.FIJO.name, valorPorDefecto = 999.0)
        )
        val resultado =
            TarifaReglaRemotaEvaluator.aplicar(reglas, "1ªdivisionfemenin", partido(), "Árbitro")
        assertNull(resultado)
    }

    @Test
    fun `gana la primera regla por orden aunque ambas coincidan`() {
        val reglas = listOf(
            TarifaReglaRemota(rol = "Árbitro", orden = 1, nombre = "Segunda", condiciones = listOf("copanavarra"), tipoCalculo = TipoCalculoTarifa.FIJO.name, valorPorDefecto = 999.0),
            TarifaReglaRemota(rol = "Árbitro", orden = 0, nombre = "Primera", condiciones = listOf("copanavarra"), tipoCalculo = TipoCalculoTarifa.FIJO.name, valorPorDefecto = 43.85)
        )
        // Se pasan desordenadas a propósito: el evaluador debe ordenar por "orden" antes de evaluar.
        val resultado =
            TarifaReglaRemotaEvaluator.aplicar(reglas, "copanavarra", partido(), "Árbitro")
        assertEquals(43.85, resultado!!, 0.001)
    }

    @Test
    fun `una regla sin condiciones nunca aplica para evitar comodines accidentales`() {
        val reglas = listOf(
            TarifaReglaRemota(rol = "Árbitro", orden = 0, condiciones = emptyList(), tipoCalculo = TipoCalculoTarifa.FIJO.name, valorPorDefecto = 999.0)
        )
        val resultado =
            TarifaReglaRemotaEvaluator.aplicar(reglas, "cualquiercosa", partido(), "Árbitro")
        assertNull(resultado)
    }
}