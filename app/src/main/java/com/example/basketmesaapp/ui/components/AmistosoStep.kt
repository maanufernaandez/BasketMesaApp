package com.example.basketmesaapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.basketmesaapp.model.CategoriaConfig
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.utils.TarifaCalculator
import com.example.basketmesaapp.utils.fadingEdge

@Composable
fun AmistosoStep(
    step: MutableState<Int>,
    categoriaId: MutableState<String>,
    local: MutableState<String>,
    visitante: MutableState<String>,
    polideportivo: MutableState<String>,
    tarifaManual: MutableState<String>,
    plusDesplazamiento: MutableState<String>,
    tieneDesplazamiento: MutableState<Boolean>,
    campoAEditar: String?,
    partidoAEditar: Partido?,
    categorias: List<CategoriaConfig>,
    onConfirm: (Partido) -> Unit,
    onDismiss: () -> Unit
) {
    var catId by categoriaId
    var loc by local
    var vis by visitante
    var poli by polideportivo
    var tarifa by tarifaManual
    var plus by plusDesplazamiento
    var tieneDespl by tieneDesplazamiento

    val listState = rememberLazyListState()
    BaseStepDialog(
        title = "Datos del Amistoso",
        onDismiss = onDismiss,
        onBack = { step.value = 3 },
        onNext = {
            if (campoAEditar != null) {
                val p = (partidoAEditar ?: Partido()).copy(
                    categoriaId = catId, equipoLocal = loc, equipoVisitante = vis,
                    polideportivo = poli, isAmistoso = true,
                    tarifaManual = tarifa.replace(",", ".").toDoubleOrNull() ?: 0.0,
                    plusDesplazamiento = if (tieneDespl) plus.replace(",", ".").toDoubleOrNull() ?: 0.0 else 0.0
                )
                onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias)))
                onDismiss()
            } else step.value = 9
        },
        nextEnabled = catId.isNotBlank() && loc.isNotBlank() && vis.isNotBlank() && poli.isNotBlank() && tarifa.isNotBlank(),
        nextText = if (campoAEditar != null) "Guardar" else "Siguiente"
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier.fillMaxWidth().fadingEdge(listState)
        ) {
            item {
                OutlinedTextField(
                    value = catId, onValueChange = { catId = it },
                    label = { Text("Categoría (Ej: Amistoso Cadete)") }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = loc, onValueChange = { loc = it },
                    label = { Text("Equipo Local") }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = vis, onValueChange = { vis = it },
                    label = { Text("Equipo Visitante") }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = poli, onValueChange = { poli = it },
                    label = { Text("Polideportivo") }, modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = tarifa,
                    onValueChange = { tarifa = it.replace(Regex("[^0-9.,]"), "") },
                    label = { Text("Tarifa del partido (€)") }, modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().clickable { tieneDespl = !tieneDespl }
                ) {
                    Checkbox(checked = tieneDespl, onCheckedChange = { tieneDespl = it })
                    Text("¿Tiene desplazamiento?", fontWeight = FontWeight.Bold)
                }
                if (tieneDespl) {
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = plus,
                        onValueChange = { plus = it.replace(Regex("[^0-9.,]"), "") },
                        label = { Text("Plus por desplazamiento (€)") }, modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
    }
}