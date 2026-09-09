package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.TarifaReglaRemota

object TarifaRulesArbitro {
    val reglas: List<TarifaReglaRemota> = TarifaDefinitions.ARBITRO.map { it.toTarifaReglaRemota() }
}