package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.TarifaReglaRemota
import com.example.basketmesaapp.model.TipoCalculoTarifa

/**
 * Definición "en crudo" de una regla de tarifa fija.
 * ÚNICA fuente de verdad: tanto la semilla que se sube a Firestore como
 * las tablas locales de fallback (sin conexión) se generan a partir de
 * esta lista. Para cambiar una tarifa, solo hay que tocar un valor aquí.
 */
data class TarifaDefinicion(
    val rol: String,
    val orden: Int,
    val nombre: String,
    val condiciones: List<String>,
    val tipoCalculo: TipoCalculoTarifa,
    val valorPorDefecto: Double,
    val numeroOficialesReferencia: Int = 1,
    val valorConReferencia: Double = 0.0,
    val valorSolitarioSinAutorizacion: Double = 0.0
)

fun TarifaDefinicion.toTarifaReglaRemota(): TarifaReglaRemota = TarifaReglaRemota(
    id = "",
    rol = rol,
    orden = orden,
    nombre = nombre,
    condiciones = condiciones,
    tipoCalculo = tipoCalculo.name,
    valorPorDefecto = valorPorDefecto,
    numeroOficialesReferencia = numeroOficialesReferencia,
    valorConReferencia = valorConReferencia,
    valorSolitarioSinAutorizacion = valorSolitarioSinAutorizacion
)

object TarifaDefinitions {
    val ARBITRO: List<TarifaDefinicion> = listOf(
        TarifaDefinicion("Árbitro", 0, "1ª División", listOf("1ªdivision"), TipoCalculoTarifa.FIJO, valorPorDefecto = 91.0),
        TarifaDefinicion("Árbitro", 1, "2ª División Femenina", listOf("2ªdivision", "femenin"), TipoCalculoTarifa.FIJO, valorPorDefecto = 56.0),
        TarifaDefinicion("Árbitro", 2, "2ª División Masculina", listOf("2ªdivision", "masculin"), TipoCalculoTarifa.FIJO, valorPorDefecto = 42.0),
        TarifaDefinicion("Árbitro", 3, "Senior 1ª", listOf("senior", "1ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 29.30, numeroOficialesReferencia = 1, valorConReferencia = 58.60),
        TarifaDefinicion("Árbitro", 4, "Senior 2ª", listOf("senior", "2ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 23.25, numeroOficialesReferencia = 1, valorConReferencia = 46.50),
        TarifaDefinicion("Árbitro", 5, "Junior 1ª", listOf("junior", "1ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 22.35, numeroOficialesReferencia = 1, valorConReferencia = 44.70),
        TarifaDefinicion("Árbitro", 6, "Junior 2ª", listOf("junior", "2ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 18.0, numeroOficialesReferencia = 1, valorConReferencia = 36.0),
        TarifaDefinicion("Árbitro", 7, "Cadete 1ª", listOf("cadete", "1ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 16.45, numeroOficialesReferencia = 1, valorConReferencia = 24.65),
        TarifaDefinicion("Árbitro", 8, "Veteranos", listOf("veteran"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 16.45, numeroOficialesReferencia = 1, valorConReferencia = 32.90),
        TarifaDefinicion("Árbitro", 9, "Copa Navarra", listOf("copanavarra"), TipoCalculoTarifa.FIJO, valorPorDefecto = 43.85),
        TarifaDefinicion("Árbitro", 10, "Selección", listOf("seleccion"), TipoCalculoTarifa.FIJO, valorPorDefecto = 10.0)
    )

    val OFICIAL_MESA: List<TarifaDefinicion> = listOf(
        TarifaDefinicion("Oficial de Mesa", 0, "Selección Navarra - Junior", listOf("seleccionnavarra", "junior"), TipoCalculoTarifa.FIJO, valorPorDefecto = 25.0),
        TarifaDefinicion("Oficial de Mesa", 1, "Selección Navarra - Cadete", listOf("seleccionnavarra", "cadete"), TipoCalculoTarifa.FIJO, valorPorDefecto = 17.60),
        TarifaDefinicion("Oficial de Mesa", 2, "Selección Navarra - Infantil", listOf("seleccionnavarra", "infantil"), TipoCalculoTarifa.FIJO, valorPorDefecto = 17.60),
        TarifaDefinicion("Oficial de Mesa", 3, "Selección Navarra - Mini", listOf("seleccionnavarra", "mini"), TipoCalculoTarifa.FIJO, valorPorDefecto = 13.40),
        TarifaDefinicion("Oficial de Mesa", 4, "Selección Navarra - Otros", listOf("seleccionnavarra"), TipoCalculoTarifa.FIJO, valorPorDefecto = 0.0),
        TarifaDefinicion("Oficial de Mesa", 5, "LF Challenge", listOf("lfchallenge"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 64.0, numeroOficialesReferencia = 4, valorConReferencia = 48.0),
        TarifaDefinicion("Oficial de Mesa", 6, "Liga EBA", listOf("ligaeba"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 38.83, numeroOficialesReferencia = 4, valorConReferencia = 29.12),
        TarifaDefinicion("Oficial de Mesa", 7, "Copa Navarra", listOf("copanavarra"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 25.45, numeroOficialesReferencia = 3, valorConReferencia = 16.65),
        TarifaDefinicion("Oficial de Mesa", 8, "2ª División Femenina", listOf("2ªdivisionfemenin"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION, valorPorDefecto = 31.60, valorConReferencia = 31.60, valorSolitarioSinAutorizacion = 47.40),
        TarifaDefinicion("Oficial de Mesa", 9, "2ª División Masculina", listOf("2ªdivisionmasculin"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION, valorPorDefecto = 25.0, valorConReferencia = 25.0, valorSolitarioSinAutorizacion = 37.50),
        TarifaDefinicion("Oficial de Mesa", 10, "Senior 1ª", listOf("senior", "1ª"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION, valorPorDefecto = 19.70, valorConReferencia = 19.70, valorSolitarioSinAutorizacion = 29.55),
        TarifaDefinicion("Oficial de Mesa", 11, "Junior 1ª", listOf("junior", "1ª"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION, valorPorDefecto = 17.0, valorConReferencia = 17.0, valorSolitarioSinAutorizacion = 25.50)
    )
}