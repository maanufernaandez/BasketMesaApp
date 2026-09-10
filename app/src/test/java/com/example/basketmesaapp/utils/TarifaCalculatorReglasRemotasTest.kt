package com.example.basketmesaapp.utils

import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.TarifaReglaRemota
import com.example.basketmesaapp.model.TipoCalculoTarifa
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Verifica la prioridad de resolución de [TarifaCalculator.calcularTotal]:
 * reglas remotas (Firestore) > reglas locales > configuración de categorías.
 */
class TarifaCalculatorReglasRemotasTest {

    @Test
    fun `sin reglas remotas se usan las reglas locales exactamente como antes`() {
        val partido = Partido(categoriaId = "1ª División Femenina", rol = "Árbitro")
        val total = TarifaCalculator.calcularTotal(partido, emptyList())
        assertEquals(94.0, total, 0.001)
    }

    @Test
    fun `una regla remota que coincide tiene prioridad sobre la tabla local`() {
        // Localmente "1ª División" vale 91.0. Simulamos que en Firestore se
        // ha subido un cambio de tarifa a 100.0 sin publicar nueva versión.
        val reglasRemotas = listOf(
            TarifaReglaRemota(rol = "Árbitro", orden = 0, condiciones = listOf("1ªdivision"), tipoCalculo = TipoCalculoTarifa.FIJO.name, valorPorDefecto = 100.0)
        )
        val partido = Partido(categoriaId = "1ª División Femenina", rol = "Árbitro")
        val total = TarifaCalculator.calcularTotal(partido, emptyList(), reglasRemotas)
        assertEquals(100.0, total, 0.001)
    }

    @Test
    fun `si las reglas remotas no cubren la categoria cae a las reglas locales`() {
        // Reglas remotas solo con una entrada que no aplica a esta categoría.
        val reglasRemotas = listOf(
            TarifaReglaRemota(rol = "Árbitro", orden = 0, condiciones = listOf("copanavarra"), tipoCalculo = TipoCalculoTarifa.FIJO.name, valorPorDefecto = 999.0)
        )
        val partido = Partido(categoriaId = "1ª División Femenina", rol = "Árbitro")
        val total = TarifaCalculator.calcularTotal(partido, emptyList(), reglasRemotas)
        // Debe usar la tabla local (94.0), no las remotas ni el fallback de config.
        assertEquals(94.0, total, 0.001)
    }

    @Test
    fun `si ni las reglas remotas ni las locales cubren la categoria cae a la config de categorias`() {
        val partido = Partido(categoriaId = "Torneo Interno Nuevo", rol = "Árbitro")
        val categorias = listOf(
            com.example.basketmesaapp.model.CategoriaConfig("Torneo Interno Nuevo", 15.0, 0.0)
        )
        val total = TarifaCalculator.calcularTotal(partido, categorias, reglasRemotas = emptyList())
        assertEquals(15.0, total, 0.001)
    }

    @Test
    fun `las reglas remotas tambien respetan el rol del partido`() {
        // Regla remota de Oficial de Mesa no debe aplicar a un Árbitro.
        val reglasRemotas = listOf(
            TarifaReglaRemota(rol = "Oficial de Mesa", orden = 0, condiciones = listOf("1ªdivision"), tipoCalculo = TipoCalculoTarifa.FIJO.name, valorPorDefecto = 999.0)
        )
        val partido = Partido(categoriaId = "1ª División Femenina", rol = "Árbitro")
        val total = TarifaCalculator.calcularTotal(partido, emptyList(), reglasRemotas)
        // Cae a la tabla local de Árbitro (94.0), ignorando la regla remota de otro rol.
        assertEquals(94.0, total, 0.001)
    }

    @Test
    fun `las reglas remotas tambien suman dieta y desplazamiento normalmente`() {
        val reglasRemotas = listOf(
            TarifaReglaRemota(
                rol = "Árbitro", orden = 0, condiciones = listOf("senior", "1ª"),
                tipoCalculo = TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name,
                valorPorDefecto = 29.30, numeroOficialesReferencia = 1, valorConReferencia = 60.0
            )
        )
        val partido = Partido(
            categoriaId = "Senior Masculino 1ª",
            rol = "Árbitro",
            numeroOficiales = 1,
            cobraDieta = true,
            tipoDesplazamiento = "Ninguno",
            plusDesplazamiento = 4.0
        )
        // tarifaBase = 60.0 (remota), dieta = 14.0 (contains "senior"), desplazamiento = 4.0
        val total = TarifaCalculator.calcularTotal(partido, emptyList(), reglasRemotas)
        assertEquals(60.0 + 14.0 + 4.0, total, 0.001)
    }

    @Test
    fun `un partido amistoso ignora las reglas remotas igual que ignoraba las locales`() {
        val reglasRemotas = listOf(
            TarifaReglaRemota(rol = "Árbitro", orden = 0, condiciones = listOf("1ªdivision"), tipoCalculo = TipoCalculoTarifa.FIJO.name, valorPorDefecto = 999.0)
        )
        val partido = Partido(
            isAmistoso = true,
            tarifaManual = 20.0,
            plusDesplazamiento = 5.0,
            categoriaId = "1ª División Femenina",
            rol = "Árbitro"
        )
        val total = TarifaCalculator.calcularTotal(partido, emptyList(), reglasRemotas)
        assertEquals(25.0, total, 0.001)
    }
}