package com.example.basketmesaapp.utils

object TarifaRulesArbitro {
    val reglas: List<TarifaRule> = listOf(
        TarifaRule(
            nombre = "1ª División",
            predicado = { it.contains("1ªdivision") || it.contains("1ªdivisión") },
            calcular = { 91.0 }
        ),
        TarifaRule(
            nombre = "2ª División Femenina",
            predicado = { (it.contains("2ªdivision") || it.contains("2ªdivisión")) && it.contains("femenin") },
            calcular = { 56.0 }
        ),
        TarifaRule(
            nombre = "2ª División Masculina",
            predicado = { (it.contains("2ªdivision") || it.contains("2ªdivisión")) && it.contains("masculin") },
            calcular = { 42.0 }
        ),
        TarifaRule(
            nombre = "Senior 1ª",
            predicado = { it.contains("senior") && it.contains("1ª") },
            calcular = { p -> if (p.numeroOficiales == 1) 58.60 else 29.30 }
        ),
        TarifaRule(
            nombre = "Senior 2ª",
            predicado = { it.contains("senior") && it.contains("2ª") },
            calcular = { p -> if (p.numeroOficiales == 1) 46.50 else 23.25 }
        ),
        TarifaRule(
            nombre = "Junior 1ª",
            predicado = { it.contains("junior") && it.contains("1ª") },
            calcular = { p -> if (p.numeroOficiales == 1) 44.70 else 22.35 }
        ),
        TarifaRule(
            nombre = "Junior 2ª",
            predicado = { it.contains("junior") && it.contains("2ª") },
            calcular = { p -> if (p.numeroOficiales == 1) 36.0 else 18.0 }
        ),
        TarifaRule(
            nombre = "Cadete 1ª",
            predicado = { it.contains("cadete") && it.contains("1ª") },
            calcular = { p -> if (p.numeroOficiales == 1) 24.65 else 16.45 }
        ),
        TarifaRule(
            nombre = "Veteranos",
            predicado = { it.contains("veteran") },
            calcular = { p -> if (p.numeroOficiales == 1) 32.90 else 16.45 }
        ),
        TarifaRule(
            nombre = "Copa Navarra",
            predicado = { it.contains("copanavarra") },
            calcular = { 43.85 }
        ),
        TarifaRule(
            nombre = "Selección",
            predicado = { it.contains("seleccion") },
            calcular = { 10.0 }
        )
    )
}