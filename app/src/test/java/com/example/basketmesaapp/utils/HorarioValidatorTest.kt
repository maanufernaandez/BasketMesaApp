package com.example.basketmesaapp.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HorarioValidatorTest {

    @Test
    fun `senior domingo antes de las 10 esta fuera de horario`() {
        // 2025-11-02 es domingo
        assertTrue(HorarioValidator.esFueraDeHorario("Senior Masculino 1ª", "2025-11-02", "09:00"))
    }

    @Test
    fun `senior domingo a las 11 esta dentro de horario`() {
        assertFalse(HorarioValidator.esFueraDeHorario("Senior Masculino 1ª", "2025-11-02", "11:00"))
    }

    @Test
    fun `cadete entre semana a las 19 esta dentro de horario`() {
        // 2025-11-04 es martes
        assertFalse(HorarioValidator.esFueraDeHorario("Cadete Masculino 1ª", "2025-11-04", "19:00"))
    }

    @Test
    fun `cadete entre semana a las 21 esta fuera de horario`() {
        assertTrue(HorarioValidator.esFueraDeHorario("Cadete Masculino 1ª", "2025-11-04", "21:00"))
    }

    @Test
    fun `seleccion nunca esta fuera de horario`() {
        assertFalse(HorarioValidator.esFueraDeHorario("Selección Navarra Junior", "2025-11-02", "08:00"))
    }

    @Test
    fun `sin fecha u hora no se puede evaluar y devuelve false`() {
        assertFalse(HorarioValidator.esFueraDeHorario("Senior Masculino 1ª", "", "10:00"))
        assertFalse(HorarioValidator.esFueraDeHorario("Senior Masculino 1ª", "2025-11-02", ""))
    }
}