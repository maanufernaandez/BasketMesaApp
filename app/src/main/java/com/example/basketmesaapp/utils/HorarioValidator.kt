package com.example.basketmesaapp.utils

/**
 * Comprueba si la hora de un partido cae fuera del horario habitual para su
 * categoría, teniendo en cuenta festivos y el día de la semana. Se extrajo
 * de AddPartidoDialog para poder testearlo sin depender de Compose.
 */
object HorarioValidator {

    fun esFueraDeHorario(catId: String, fechaStr: String, horaStr: String): Boolean {
        if (fechaStr.isEmpty() || horaStr.isEmpty() || !horaStr.contains(":")) return false
        val base = catId.lowercase()
        if (base.contains("seleccion")) return false

        val isSenior = base.contains("senior") || base.contains("2ª division mas") || base.contains("2ª división mas")
        val isJunior = base.contains("junior")
        val isCadete = base.contains("cadete")

        if (!isSenior && !isJunior && !isCadete) return false

        val parts = horaStr.split(":")
        val horaNum = parts[0].toIntOrNull() ?: return false
        val minNum = parts[1].toIntOrNull() ?: return false
        val mins = horaNum * 60 + minNum

        val cal = java.util.Calendar.getInstance().apply {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            time = sdf.parse(fechaStr) ?: return false
        }
        val dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK)
        val esFestivo = DataConstants.festivosTemporada.contains(fechaStr)

        return when {
            dayOfWeek == java.util.Calendar.SUNDAY || esFestivo -> {
                // Si no es senior ni junior, el guard de arriba garantiza que es cadete.
                if (isSenior || isJunior) mins < 10 * 60 || (mins >= 12 * 60 + 30 && mins < 16 * 60) || mins > 18 * 60
                else mins < 10 * 60 || mins > 12 * 60 + 30
            }
            dayOfWeek == java.util.Calendar.SATURDAY -> {
                when {
                    isSenior -> mins < 16 * 60 || mins > 20 * 60 + 30
                    isJunior -> mins < 10 * 60 || (mins >= 13 * 60 && mins < 16 * 60) || mins > 20 * 60 + 30
                    // Si no es senior ni junior, el guard de arriba garantiza que es cadete.
                    else -> mins < 9 * 60 || (mins >= 13 * 60 + 20 && mins < 16 * 60) || mins > 20 * 60 + 30
                }
            }
            else -> mins < 18 * 60 || mins >= 20 * 60 + 30
        }
    }
}