package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.TarifaReglaRemota

object TarifaRulesOficialMesa {
    val reglas: List<TarifaReglaRemota> = TarifaDefinitions.OFICIAL_MESA.map { it.toTarifaReglaRemota() }
}