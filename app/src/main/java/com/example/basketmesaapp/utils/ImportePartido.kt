package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido

/**
 * Decide si, al guardar un partido ya existente, hay que volver a calcular
 * su importe. Si no ha cambiado nada que influya en el precio (por ejemplo,
 * solo se ha corregido un equipo), se conserva el importe guardado: así una
 * subida de tarifas no altera en silencio partidos antiguos al editarlos.
 */
object ImportePartido {

    fun cambiaElImporte(anterior: Partido, nuevo: Partido): Boolean =
        anterior.categoriaId != nuevo.categoriaId ||
                anterior.rol != nuevo.rol ||
                anterior.numeroOficiales != nuevo.numeroOficiales ||
                anterior.cobraDieta != nuevo.cobraDieta ||
                anterior.tipoDesplazamiento != nuevo.tipoDesplazamiento ||
                anterior.polideportivo != nuevo.polideportivo ||
                anterior.plusDesplazamiento != nuevo.plusDesplazamiento ||
                anterior.isAmistoso != nuevo.isAmistoso ||
                anterior.tarifaManual != nuevo.tarifaManual ||
                anterior.autorizado3Vistas != nuevo.autorizado3Vistas
}