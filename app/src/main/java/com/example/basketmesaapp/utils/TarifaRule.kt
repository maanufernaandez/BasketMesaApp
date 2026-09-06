package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido

data class TarifaRule(
    val nombre: String,
    val predicado: (categoriaNormalizada: String) -> Boolean,
    val calcular: (partido: Partido) -> Double
)


fun List<TarifaRule>.aplicar(categoriaNormalizada: String, partido: Partido): Double? =
    firstOrNull { it.predicado(categoriaNormalizada) }?.calcular?.invoke(partido)