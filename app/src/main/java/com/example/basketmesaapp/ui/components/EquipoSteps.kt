package com.example.basketmesaapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basketmesaapp.model.CategoriaConfig
import com.example.basketmesaapp.model.Equipo
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.utils.TarifaCalculator
import com.example.basketmesaapp.utils.fadingEdge

@Composable
fun EquipoSteps(
    step: MutableState<Int>,
    polideportivo: MutableState<String>,
    local: MutableState<String>,
    visitante: MutableState<String>,
    invertirLocalia: MutableState<Boolean>,
    teamsInCategory: List<Equipo>,
    campoAEditar: String?,
    partidoAEditar: Partido?,
    userRol: String,
    autorizado3Vistas: Boolean,
    categorias: List<CategoriaConfig>,
    requiresOfficialSelection: Boolean,
    onConfirm: (Partido) -> Unit,
    onDismiss: () -> Unit
) {
    when (step.value) {
        4 -> {
            val listState = rememberLazyListState()
            val availablePolis = teamsInCategory.flatMap { it.polideportivos }.distinct().sorted()
            val displayPolis = listOf("Otro") + availablePolis
            BaseStepDialog(title = "Polideportivo", onDismiss = onDismiss, onBack = { step.value = 3 }, onNext = null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        modifier = Modifier.fillMaxSize().fadingEdge(listState)
                    ) {
                        items(displayPolis) { poli ->
                            TextButton(
                                onClick = {
                                    if (poli == "Otro") {
                                        polideportivo.value = ""; step.value = 45
                                    } else {
                                        polideportivo.value = poli
                                        if (campoAEditar != null) {
                                            val p = (partidoAEditar ?: Partido()).copy(
                                                polideportivo = polideportivo.value, rol = userRol, autorizado3Vistas = autorizado3Vistas
                                            )
                                            onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias)))
                                            onDismiss()
                                        } else {
                                            val localCandidates = teamsInCategory.filter { it.polideportivos.contains(poli) }
                                            if (localCandidates.size == 1) { local.value = localCandidates[0].nombre; step.value = 6 } else step.value = 5
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) { Text(poli, fontSize = 17.sp, fontWeight = FontWeight.Medium) }
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        }
                    }
                }
            }
        }
        45 -> {
            var poli by polideportivo
            BaseStepDialog(
                title = "Escribe el Polideportivo",
                onDismiss = onDismiss,
                onBack = { step.value = 4 },
                onNext = { step.value = 5 },
                nextEnabled = poli.isNotBlank()
            ) {
                OutlinedTextField(
                    value = poli,
                    onValueChange = { poli = it },
                    label = { Text("Escribe el nombre del polideportivo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
        5 -> {
            val listState = rememberLazyListState()
            val availablePolis = teamsInCategory.flatMap { it.polideportivos }.distinct()
            val localCandidates = if (availablePolis.contains(polideportivo.value))
                teamsInCategory.filter { it.polideportivos.contains(polideportivo.value) }
            else teamsInCategory
            val filteredCandidates = localCandidates.filter { it.nombre != "Visitante" }
            BaseStepDialog(
                title = "Equipo Local",
                onDismiss = onDismiss,
                onBack = { step.value = if (availablePolis.contains(polideportivo.value)) 4 else 45 }
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        modifier = Modifier.fillMaxSize().fadingEdge(listState)
                    ) {
                        items(filteredCandidates) { team ->
                            TextButton(
                                onClick = { local.value = team.nombre; step.value = 6 },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) { Text(team.nombre, fontSize = 17.sp) }
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        }
                    }
                }
            }
        }
        6 -> {
            val listState = rememberLazyListState()
            val availablePolis = teamsInCategory.flatMap { it.polideportivos }.distinct()
            val visitorCandidates = teamsInCategory.filter { it.nombre != local.value }
            var invLocalia by invertirLocalia
            BaseStepDialog(
                title = "Equipo Visitante",
                onDismiss = onDismiss,
                onBack = {
                    val localCandidates = teamsInCategory.filter { it.polideportivos.contains(polideportivo.value) }
                    step.value = if (localCandidates.size == 1 && availablePolis.contains(polideportivo.value)) 4 else 5
                }
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { invLocalia = !invLocalia }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Checkbox(checked = invLocalia, onCheckedChange = { invLocalia = it })
                            Text(
                                text = "El equipo visitante actuará como LOCAL",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(vertical = 8.dp),
                            modifier = Modifier.fillMaxSize().fadingEdge(listState)
                        ) {
                            items(visitorCandidates) { team ->
                                TextButton(
                                    onClick = {
                                        visitante.value = team.nombre
                                        if (campoAEditar != null) {
                                            val finalLocal = if (invLocalia) visitante.value else local.value
                                            val finalVisitante = if (invLocalia) local.value else visitante.value
                                            val p = (partidoAEditar ?: Partido()).copy(
                                                equipoLocal = finalLocal, equipoVisitante = finalVisitante,
                                                rol = userRol, autorizado3Vistas = autorizado3Vistas
                                            )
                                            onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias)))
                                            onDismiss()
                                        } else step.value = if (requiresOfficialSelection) 7 else 8
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                ) { Text(team.nombre, fontSize = 17.sp) }
                                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}