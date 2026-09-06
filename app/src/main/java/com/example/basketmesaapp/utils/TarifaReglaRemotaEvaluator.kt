package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.TarifaReglaRemota
import com.example.basketmesaapp.model.TipoCalculoTarifa

object TarifaReglaRemotaEvaluator {

    fun aplicar(
        reglas: List<TarifaReglaRemota>,
        categoriaNormalizada: String,
        partido: Partido,
        rol: String
    ): Double? {
        val reglaAplicable = reglas
            .filter { it.rol == rol }
            .sortedBy { it.orden }
            .firstOrNull { regla ->
                regla.condiciones.isNotEmpty() && regla.condiciones.all { categoriaNormalizada.contains(it) }
            } ?: return null

        return calcular(reglaAplicable, partido)
    }

    private fun calcular(regla: TarifaReglaRemota, partido: Partido): Double {
        return when (TipoCalculoTarifa.fromString(regla.tipoCalculo)) {
            TipoCalculoTarifa.FIJO -> regla.valorPorDefecto

            TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES ->
                if (partido.numeroOficiales == regla.numeroOficialesReferencia) {
                    regla.valorConReferencia
                } else {
                    regla.valorPorDefecto
                }

            TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION ->
                if (partido.numeroOficiales == 1) {
                    if (partido.autorizado3Vistas) regla.valorConReferencia * 2 else regla.valorSolitarioSinAutorizacion
                } else {
                    regla.valorPorDefecto
                }
        }
    }
}