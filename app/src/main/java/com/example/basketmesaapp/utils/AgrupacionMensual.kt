package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.Sancion
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

data class MesResumen(
    val clave: String,
    val nombre: String,
    val partidos: List<Partido>,
    val sanciones: List<Sancion>,
    val totalSanciones: Double,
    val totalNeto: Double
)

/**
 * Agrupa partidos y sanciones por mes (más reciente primero). Se calcula
 * fuera del Composable para no repetir el trabajo en cada recomposición y
 * poder testearlo.
 */
object AgrupacionMensual {

    private val formatoMes = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.forLanguageTag("es-ES"))

    fun claveDeMes(fecha: String): String = if (fecha.length >= 7) fecha.substring(0, 7) else "0000-00"

    fun nombreDeMes(clave: String): String = try {
        YearMonth.parse(clave).format(formatoMes).replaceFirstChar { it.uppercase() }
    } catch (e: Exception) {
        "Mes Desconocido"
    }

    fun agrupar(partidos: List<Partido>, sanciones: List<Sancion>): List<MesResumen> {
        val claves = (partidos.map { claveDeMes(it.fecha) } + sanciones.map { claveDeMes(it.fecha) })
            .distinct()
            .sortedDescending()

        return claves.map { clave ->
            val partidosMes = partidos
                .filter { claveDeMes(it.fecha) == clave }
                .sortedWith(compareByDescending<Partido> { it.fecha }.thenByDescending { it.hora })
            val sancionesMes = sanciones
                .filter { claveDeMes(it.fecha) == clave }
                .sortedByDescending { it.fecha }
            val totalPartidos = partidosMes.sumOf { it.totalPartido }
            val totalSanciones = sancionesMes.sumOf { it.importe }

            MesResumen(
                clave = clave,
                nombre = nombreDeMes(clave),
                partidos = partidosMes,
                sanciones = sancionesMes,
                totalSanciones = totalSanciones,
                totalNeto = totalPartidos - totalSanciones
            )
        }
    }
}