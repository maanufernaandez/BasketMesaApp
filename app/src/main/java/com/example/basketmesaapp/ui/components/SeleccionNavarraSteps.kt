package com.example.basketmesaapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basketmesaapp.model.CategoriaConfig
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.utils.TarifaCalculator
import com.example.basketmesaapp.utils.fadingEdge

/**
 * Pasos 31-34 del wizard de creación de partido: subcategoría, polideportivo
 * y tarifa manual, específicos del flujo de Selección Navarra. Extraído de
 * AddPartidoDialog para mantener ese archivo más pequeño.
 */
@Composable
fun SeleccionNavarraSteps(
    step: MutableState<Int>,
    categoriaId: MutableState<String>,
    numOficiales: MutableState<Int>,
    local: MutableState<String>,
    visitante: MutableState<String>,
    polideportivo: MutableState<String>,
    tarifaManual: MutableState<String>,
    userRol: String,
    campoAEditar: String?,
    partidoAEditar: Partido?,
    categorias: List<CategoriaConfig>,
    onConfirm: (Partido) -> Unit,
    onDismiss: () -> Unit
) {
    when (step.value) {
        31 -> {
            val subcats = listOf(
                "Junior Masculino", "Junior Femenino", "Cadete Masculino", "Cadete Femenino",
                "Infantil Masculino", "Infantil Femenino", "Mini Masculino", "Mini Femenino"
            )
            val listState = rememberLazyListState()
            BaseStepDialog(title = "Selección Navarra", onDismiss = onDismiss, onBack = { step.value = 3 }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        modifier = Modifier.fillMaxSize().fadingEdge(listState)
                    ) {
                        items(subcats) { sub ->
                            TextButton(
                                onClick = {
                                    categoriaId.value = "Selección Navarra $sub"
                                    numOficiales.value = if (userRol == "Árbitro") 2 else 1
                                    local.value = ""
                                    visitante.value = ""
                                    step.value = 32
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) { Text(sub, fontSize = 17.sp, fontWeight = FontWeight.Bold) }
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        }
                    }
                }
            }
        }
        32 -> {
            BaseStepDialog(title = "Polideportivo", onDismiss = onDismiss, onBack = { step.value = 31 }) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(
                        onClick = { polideportivo.value = "Larrabide"; step.value = 34 },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Larrabide", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                    HorizontalDivider()
                    TextButton(
                        onClick = { polideportivo.value = ""; step.value = 33 },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Otros", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
        33 -> {
            var poli by polideportivo
            BaseStepDialog(
                title = "Escribe el Polideportivo",
                onDismiss = onDismiss,
                onBack = { step.value = 32 },
                onNext = { step.value = 34 },
                nextEnabled = poli.isNotBlank()
            ) {
                OutlinedTextField(
                    value = poli,
                    onValueChange = { poli = it },
                    label = { Text("Nombre del pabellón") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
        34 -> {
            var tarifa by tarifaManual
            BaseStepDialog(
                title = "Tarifa",
                onDismiss = onDismiss,
                onBack = { step.value = if (polideportivo.value == "Larrabide") 32 else 33 },
                onNext = {
                    if (campoAEditar != null) {
                        val p = (partidoAEditar ?: Partido()).copy(
                            tarifaManual = tarifa.replace(",", ".").toDoubleOrNull() ?: 0.0
                        )
                        onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias)))
                        onDismiss()
                    } else step.value = 8
                },
                nextEnabled = tarifa.replace(",", ".").toDoubleOrNull() != null,
                nextText = if (campoAEditar != null) "Guardar" else "Siguiente"
            ) {
                OutlinedTextField(
                    value = tarifa,
                    onValueChange = { tarifa = it.replace(Regex("[^0-9.,]"), "") },
                    label = { Text("Tarifa del partido (€)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}