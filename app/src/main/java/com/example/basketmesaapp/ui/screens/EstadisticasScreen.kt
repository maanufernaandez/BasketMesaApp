package com.example.basketmesaapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.Sancion

@Composable
fun EstadisticasScreen(
    partidos: List<Partido>,
    sanciones: List<Sancion>,
    userRol: String,
    onDismiss: () -> Unit
) {
    val localeSpanish = java.util.Locale("es", "ES")

    fun getMonthKey(fecha: String): String = if (fecha.length >= 7) fecha.substring(0, 7) else "0000-00"
    fun getMonthName(key: String): String {
        return try {
            val date = java.text.SimpleDateFormat("yyyy-MM", localeSpanish).parse(key)
            java.text.SimpleDateFormat("MMMM yyyy", localeSpanish).format(date!!).replaceFirstChar { c -> c.uppercase() }
        } catch (e: Exception) { "Mes Desconocido" }
    }

    val allKeys = (partidos.map { getMonthKey(it.fecha) } + sanciones.map { getMonthKey(it.fecha) }).distinct().sortedDescending()
    val expandedStates = remember { androidx.compose.runtime.mutableStateMapOf<String, Boolean>() }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        containerColor = MaterialTheme.colorScheme.background,
        title = { Text("Estadísticas", fontWeight = FontWeight.ExtraBold) },
        text = {
            LazyColumn(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                item {
                    StatMenuSection(
                        title = "TOTAL TEMPORADA",
                        datos = partidos,
                        sanciones = sanciones,
                        isExpanded = expandedStates["TOTAL"] ?: false,
                        userRol = userRol,
                        onToggle = { expandedStates["TOTAL"] = !(expandedStates["TOTAL"] ?: false) }
                    )
                }
                allKeys.forEach { mesKey ->
                    val mesName = getMonthName(mesKey)
                    val partidosMes = partidos.filter { getMonthKey(it.fecha) == mesKey }
                    val sancionesMes = sanciones.filter { getMonthKey(it.fecha) == mesKey }
                    item {
                        StatMenuSection(
                            title = mesName.uppercase(),
                            datos = partidosMes,
                            sanciones = sancionesMes,
                            isExpanded = expandedStates[mesKey] ?: false,
                            userRol = userRol,
                            onToggle = { expandedStates[mesKey] = !(expandedStates[mesKey] ?: false) }
                        )
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Cerrar", color = MaterialTheme.colorScheme.primary) } }
    )
}

@Composable
fun StatMenuSection(
    title: String,
    datos: List<Partido>,
    sanciones: List<Sancion>,
    isExpanded: Boolean,
    userRol: String,
    onToggle: () -> Unit
) {
    val totalGanadoPartidos = datos.sumOf { it.totalPartido }
    val totalPerdidoSanciones = sanciones.sumOf { it.importe }
    val totalGanado = totalGanadoPartidos - totalPerdidoSanciones
    val totalGanadoFormateado = String.format(java.util.Locale.US, "%.2f", totalGanado)

    val totalPartidos = datos.size
    val totalDesplazamientos = datos.count { it.tipoDesplazamiento != "Ninguno" }

    val catsFlexible = listOf(
        "lfchallenge", "ligaeba", "copanavarra",
        "2ªdivisionmasculin", "2ªdivisionfemenin",
        "seniormasculin1ª", "seniorfemenin1ª",
        "juniormasculin1ª", "juniorfemenin1ª"
    )

    val total3Funciones = datos.count { partido ->
        val normalizedId = partido.categoriaId.lowercase()
            .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
            .replace("masculino", "masculin").replace("masculina", "masculin")
            .replace("femenino", "femenin").replace("femenina", "femenin")
            .replace(" ", "").replace("/", "").replace("-", "")

        val isFlexible = catsFlexible.any { normalizedId.contains(it) || it.contains(normalizedId) }

        partido.rol == "Oficial de Mesa" && partido.numeroOficiales == 1 && partido.autorizado3Vistas && isFlexible
    }

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Surface(
            modifier = Modifier.fillMaxWidth().clickable { onToggle() },
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Desplegar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = title, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "$totalGanadoFormateado €", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Black, maxLines = 1, softWrap = false)
            }
        }

        if (isExpanded) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp, start = 4.dp, end = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(text = "$totalPartidos", fontSize = 22.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Partidos", fontSize = 11.sp, color = Color.LightGray, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center, maxLines = 1)
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Text(text = "$totalDesplazamientos", fontSize = 22.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Viajes", fontSize = 11.sp, color = Color.LightGray, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center, maxLines = 1)
                    }
                }

                if (userRol == "Oficial de Mesa") {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Text(text = "$total3Funciones", fontSize = 22.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "3 Vistas", fontSize = 11.sp, color = Color.LightGray, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center, maxLines = 1)
                        }
                    }
                }
            }
        }
    }
}