package com.example.basketmesaapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.utils.DataConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidoCard(partido: Partido, onEdit: (Partido, String?) -> Unit, onDelete: (String) -> Unit) {
    val localeSpanish = java.util.Locale("es", "ES")
    var showSelectionSheet by remember { mutableStateOf(false) }

    val formatPrice = { price: Double ->
        String.format(java.util.Locale.US, "%.2f€", price)
    }

    var showInfoDialog by remember { mutableStateOf(false) }
    var infoTitle by remember { mutableStateOf("") }
    var infoMessage by remember { mutableStateOf("") }

    val fechaFormateada = try {
        val dateObj = java.text.SimpleDateFormat("yyyy-MM-dd", localeSpanish).parse(partido.fecha)
        val formatDia = java.text.SimpleDateFormat("EEEE d", localeSpanish).format(dateObj!!)
        formatDia.replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeSpanish) else it.toString() }
    } catch (e: Exception) { partido.fecha }

    val flexible = listOf(
        "lfchallenge", "ligaeba", "copanavarra",
        "2ªdivisionmasculin", "2ªdivisionfemenin",
        "seniormasculin1ª", "seniorfemenin1ª",
        "juniormasculin1ª", "juniorfemenin1ª"
    )
    val normalizedId = partido.categoriaId.lowercase()
        .replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u")
        .replace("masculino", "masculin").replace("masculina", "masculin")
        .replace("femenino", "femenin").replace("femenina", "femenin")
        .replace(" ", "").replace("/", "").replace("-", "")

    val requiresOfficialSelection = flexible.any { normalizedId.contains(it) || it.contains(normalizedId) }
    val esSeleccion = partido.categoriaId.startsWith("Selección Navarra", ignoreCase = true)
    val textoCategoriaPill = if (esSeleccion) "SELECCIÓN NAVARRA" else partido.categoriaId.uppercase()
    val textoEquiposPpal = if (esSeleccion) {
        partido.categoriaId.replace("Selección Navarra", "", ignoreCase = true).trim()
    } else {
        "${partido.equipoLocal} - ${partido.equipoVisitante}"
    }

    if (showSelectionSheet) {
        ModalBottomSheet(onDismissRequest = { showSelectionSheet = false }) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text("¿Qué deseas editar?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                val labelDinamico = if (partido.rol == "Árbitro") "Árbitros" else "Oficiales"
                val opciones = listOf("Fecha", "Hora", labelDinamico)

                opciones.forEach { opcion ->
                    TextButton(
                        onClick = {
                            showSelectionSheet = false
                            onEdit(partido, opcion)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(opcion, fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text(infoTitle, fontWeight = FontWeight.Bold) },
            text = { Text(infoMessage) },
            confirmButton = { TextButton(onClick = { showInfoDialog = false }) { Text("Aceptar") } }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = textoCategoriaPill,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "+${formatPrice(partido.totalPartido)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF4ADE80)
                )
            }

            Text(
                text = textoEquiposPpal,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp),
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$fechaFormateada - ${partido.hora}",
                            fontSize = 14.sp,
                            color = Color.LightGray,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pdvo. ${partido.polideportivo}",
                            fontSize = 14.sp,
                            color = Color.LightGray,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            lineHeight = 16.sp,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    val mostrarEtiquetaOficiales = (partido.rol == "Oficial de Mesa" && partido.numeroOficiales == 1 && requiresOfficialSelection) ||
                            (partido.rol == "Árbitro" && partido.numeroOficiales == 1)

                    if (mostrarEtiquetaOficiales) {
                        val textoEtiqueta = if (partido.rol == "Árbitro") "1 Árbitro" else {
                            if (partido.autorizado3Vistas) "3 Funciones Vistas" else "1 Oficial"
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFF97316).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = textoEtiqueta,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 12.sp,
                                color = Color(0xFFEA580C),
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    if (partido.tipoDesplazamiento != "Ninguno") {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            modifier = Modifier.clickable {
                                val match = DataConstants.preciosDesplazamiento.entries.find { partido.polideportivo.contains(it.key, ignoreCase = true) }
                                val importe = if (partido.tipoDesplazamiento == "Conductor") match?.value?.first else match?.value?.second
                                infoTitle = if (partido.tipoDesplazamiento == "Conductor") "Cobro conductor" else "Cobro acompañante"
                                infoMessage = formatPrice(importe ?: partido.plusDesplazamiento)
                                showInfoDialog = true
                            }
                        ) {
                            Text(
                                text = if (partido.tipoDesplazamiento == "Conductor") "🚗 Conductor" else "👤 Acompañante",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    if (partido.cobraDieta) {
                        val baseExt = partido.categoriaId.lowercase()
                        val montoLabel = when {
                            baseExt.contains("senior") || baseExt.contains("2ª division mas") || baseExt.contains("2ª división mas") -> "14"
                            baseExt.contains("junior") -> "10"
                            baseExt.contains("cadete") -> "5"
                            else -> "0"
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Dietas (${montoLabel}€)",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { showSelectionSheet = true }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                }
                IconButton(onClick = { onDelete(partido.id) }, modifier = Modifier.size(36.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color(0xFFEF4444).copy(alpha = 0.8f))
                }
            }
        }
    }
}