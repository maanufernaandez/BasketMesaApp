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
    /**
     * Súbela en 1 cada vez que cambies algún valor de ARBITRO u OFICIAL_MESA.
     * Al arrancar, la app compara este número con el guardado en Firestore;
     * si no coincide, borra las reglas viejas y vuelve a sembrar con las
     * nuevas — así los cambios llegan sin tener que tocar la consola de
     * Firebase a mano.
     */
    const val VERSION = 2L
    val ARBITRO: List<TarifaDefinicion> = listOf(
        TarifaDefinicion("Árbitro", 0, "1ª División", listOf("1ªdivision"), TipoCalculoTarifa.FIJO, valorPorDefecto = 94.0),
        TarifaDefinicion("Árbitro", 1, "2ª División Femenina", listOf("2ªdivision", "femenin"), TipoCalculoTarifa.FIJO, valorPorDefecto = 58.0),
        TarifaDefinicion("Árbitro", 2, "2ª División Masculina", listOf("2ªdivision", "masculin"), TipoCalculoTarifa.FIJO, valorPorDefecto = 42.85),
        TarifaDefinicion("Árbitro", 3, "Senior 1ª", listOf("senior", "1ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 29.90, numeroOficialesReferencia = 1, valorConReferencia = 59.80),
        TarifaDefinicion("Árbitro", 4, "Senior 2ª", listOf("senior", "2ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 23.70, numeroOficialesReferencia = 1, valorConReferencia = 47.40),
        TarifaDefinicion("Árbitro", 5, "Junior 1ª", listOf("junior", "1ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 22.80, numeroOficialesReferencia = 1, valorConReferencia = 45.60),
        TarifaDefinicion("Árbitro", 6, "Junior 2ª", listOf("junior", "2ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 18.35, numeroOficialesReferencia = 1, valorConReferencia = 36.70),
        TarifaDefinicion("Árbitro", 7, "Cadete 1ª", listOf("cadete", "1ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 16.80, numeroOficialesReferencia = 1, valorConReferencia = 25.20),
        TarifaDefinicion("Árbitro", 8, "Veteranos", listOf("veteran"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 16.80, numeroOficialesReferencia = 1, valorConReferencia = 33.60),
        TarifaDefinicion("Árbitro", 9, "Copa Navarra", listOf("copanavarra"), TipoCalculoTarifa.FIJO, valorPorDefecto = 44.75),
        TarifaDefinicion("Árbitro", 10, "Selección", listOf("seleccion"), TipoCalculoTarifa.FIJO, valorPorDefecto = 10.0),
        TarifaDefinicion("Árbitro", 11, "LF Challenge", listOf("lfchallenge"), TipoCalculoTarifa.FIJO, valorPorDefecto = 270.0),
        TarifaDefinicion("Árbitro", 12, "Liga EBA", listOf("ligaeba"), TipoCalculoTarifa.FIJO, valorPorDefecto = 140.0)
    )

    val OFICIAL_MESA: List<TarifaDefinicion> = listOf(
        TarifaDefinicion("Oficial de Mesa", 0, "Selección Navarra - Junior", listOf("seleccionnavarra", "junior"), TipoCalculoTarifa.FIJO, valorPorDefecto = 25.0),
        TarifaDefinicion("Oficial de Mesa", 1, "Selección Navarra - Cadete", listOf("seleccionnavarra", "cadete"), TipoCalculoTarifa.FIJO, valorPorDefecto = 17.60),
        TarifaDefinicion("Oficial de Mesa", 2, "Selección Navarra - Infantil", listOf("seleccionnavarra", "infantil"), TipoCalculoTarifa.FIJO, valorPorDefecto = 17.60),
        TarifaDefinicion("Oficial de Mesa", 3, "Selección Navarra - Mini", listOf("seleccionnavarra", "mini"), TipoCalculoTarifa.FIJO, valorPorDefecto = 13.40),
        TarifaDefinicion("Oficial de Mesa", 4, "Selección Navarra - Otros", listOf("seleccionnavarra"), TipoCalculoTarifa.FIJO, valorPorDefecto = 0.0),
        TarifaDefinicion("Oficial de Mesa", 5, "LF Challenge", listOf("lfchallenge"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 64.0, numeroOficialesReferencia = 4, valorConReferencia = 48.0),
        TarifaDefinicion("Oficial de Mesa", 6, "Liga EBA", listOf("ligaeba"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 38.83, numeroOficialesReferencia = 4, valorConReferencia = 29.12),
        TarifaDefinicion("Oficial de Mesa", 7, "1ª División", listOf("1ªdivision"), TipoCalculoTarifa.FIJO, valorPorDefecto = 40.0),
        TarifaDefinicion("Oficial de Mesa", 8, "Copa Navarra", listOf("copanavarra"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 25.45, numeroOficialesReferencia = 3, valorConReferencia = 16.95),
        TarifaDefinicion("Oficial de Mesa", 9, "2ª División Femenina", listOf("2ªdivisionfemenin"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION, valorPorDefecto = 32.0, valorConReferencia = 32.0, valorSolitarioSinAutorizacion = 64.0),
        TarifaDefinicion("Oficial de Mesa", 10, "2ª División Masculina", listOf("2ªdivisionmasculin"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION, valorPorDefecto = 25.50, valorConReferencia = 25.50, valorSolitarioSinAutorizacion = 51.0),
        TarifaDefinicion("Oficial de Mesa", 11, "Senior 1ª", listOf("senior", "1ª"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION, valorPorDefecto = 20.10, valorConReferencia = 20.10, valorSolitarioSinAutorizacion = 40.20),
        TarifaDefinicion("Oficial de Mesa", 12, "Senior 2ª", listOf("senior", "2ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 12.40, numeroOficialesReferencia = 1, valorConReferencia = 19.55),
        TarifaDefinicion("Oficial de Mesa", 13, "Junior 1ª", listOf("junior", "1ª"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION, valorPorDefecto = 17.0, valorConReferencia = 17.0, valorSolitarioSinAutorizacion = 25.50),
        TarifaDefinicion("Oficial de Mesa", 14, "Junior 2ª", listOf("junior", "2ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES, valorPorDefecto = 11.45, numeroOficialesReferencia = 1, valorConReferencia = 16.20),
        TarifaDefinicion("Oficial de Mesa", 15, "Cadete 1ª", listOf("cadete", "1ª"), TipoCalculoTarifa.FIJO, valorPorDefecto = 11.55),
        TarifaDefinicion("Oficial de Mesa", 16, "Veteranos", listOf("veteran"), TipoCalculoTarifa.FIJO, valorPorDefecto = 13.60)
    )
}