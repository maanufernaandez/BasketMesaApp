package com.example.basketmesaapp.utils

object TarifaRulesOficialMesa {
    val reglas: List<TarifaRule> = listOf(
        TarifaRule(
            nombre = "Selección Navarra - Junior",
            predicado = { it.contains("seleccionnavarra") && it.contains("junior") },
            calcular = { 25.0 }
        ),
        TarifaRule(
            nombre = "Selección Navarra - Cadete",
            predicado = { it.contains("seleccionnavarra") && it.contains("cadete") },
            calcular = { 17.60 }
        ),
        TarifaRule(
            nombre = "Selección Navarra - Infantil",
            predicado = { it.contains("seleccionnavarra") && it.contains("infantil") },
            calcular = { 17.60 }
        ),
        TarifaRule(
            nombre = "Selección Navarra - Mini",
            predicado = { it.contains("seleccionnavarra") && it.contains("mini") },
            calcular = { 13.40 }
        ),
        TarifaRule(
            // Reproduce el "else -> 0.0" del when anidado original: cualquier
            // otra categoría de Selección Navarra no contemplada explícitamente.
            nombre = "Selección Navarra - Otros",
            predicado = { it.contains("seleccionnavarra") },
            calcular = { 0.0 }
        ),
        TarifaRule(
            nombre = "LF Challenge",
            predicado = { it.contains("lfchallenge") },
            calcular = { p -> if (p.numeroOficiales == 4) 48.0 else 64.0 }
        ),
        TarifaRule(
            nombre = "Liga EBA",
            predicado = { it.contains("ligaeba") },
            calcular = { p -> if (p.numeroOficiales == 4) 29.12 else 38.83 }
        ),
        TarifaRule(
            nombre = "Copa Navarra",
            predicado = { it.contains("copanavarra") },
            calcular = { p -> if (p.numeroOficiales == 3) 16.65 else 25.45 }
        ),
        TarifaRule(
            nombre = "2ª División Femenina",
            predicado = { it.contains("2ªdivisionfemenin") },
            calcular = { p ->
                if (p.numeroOficiales == 1) {
                    if (p.autorizado3Vistas) 31.60 * 2 else 47.40
                } else 31.60
            }
        ),
        TarifaRule(
            nombre = "2ª División Masculina",
            predicado = { it.contains("2ªdivisionmasculin") },
            calcular = { p ->
                if (p.numeroOficiales == 1) {
                    if (p.autorizado3Vistas) 25.0 * 2 else 37.50
                } else 25.0
            }
        ),
        TarifaRule(
            nombre = "Senior 1ª",
            predicado = { it.contains("senior") && it.contains("1ª") },
            calcular = { p ->
                if (p.numeroOficiales == 1) {
                    if (p.autorizado3Vistas) 19.70 * 2 else 29.55
                } else 19.70
            }
        ),
        TarifaRule(
            nombre = "Junior 1ª",
            predicado = { it.contains("junior") && it.contains("1ª") },
            calcular = { p ->
                if (p.numeroOficiales == 1) {
                    if (p.autorizado3Vistas) 17.0 * 2 else 25.50
                } else 17.0
            }
        )
    )
}