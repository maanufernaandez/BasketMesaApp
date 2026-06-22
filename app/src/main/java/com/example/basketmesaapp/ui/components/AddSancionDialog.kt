package com.example.basketmesaapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.basketmesaapp.model.Sancion
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddSancionDialog(
    sancionAEditar: Sancion? = null,
    onDismiss: () -> Unit,
    onConfirm: (Sancion) -> Unit
) {
    var step by remember(sancionAEditar) { mutableStateOf(1) }

    var fecha by remember(sancionAEditar) { mutableStateOf(sancionAEditar?.fecha ?: "") }
    var motivo by remember(sancionAEditar) { mutableStateOf(sancionAEditar?.motivo ?: "") }
    var importeStr by remember(sancionAEditar) { mutableStateOf(if (sancionAEditar != null && sancionAEditar.importe > 0.0) sancionAEditar.importe.toString() else "") }

    when (step) {
        1 -> {
            var fechaTemporal by remember { mutableStateOf(fecha) }
            BaseStepDialog(
                title = "Fecha de Sanción",
                onDismiss = onDismiss,
                onBack = null,
                onNext = { fecha = fechaTemporal; step = 2 },
                nextEnabled = fechaTemporal.isNotEmpty()
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CustomDatePicker(initialDate = fechaTemporal) { nuevaFecha -> fechaTemporal = nuevaFecha }
                }
            }
        }
        2 -> {
            BaseStepDialog(
                title = "Detalles de Sanción",
                onDismiss = onDismiss,
                onBack = { step = 1 },
                onNext = {
                    val importeDouble = importeStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val sancionGenerada = Sancion(
                        fecha = fecha,
                        motivo = motivo,
                        importe = importeDouble,
                        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    )

                    val sancionFinal = if (sancionAEditar != null) sancionGenerada.copy(id = sancionAEditar.id) else sancionGenerada
                    onConfirm(sancionFinal)
                },
                nextText = "Guardar",
                nextEnabled = motivo.isNotBlank() && importeStr.isNotBlank()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = motivo,
                        onValueChange = { motivo = it },
                        label = { Text("Motivo de la sanción") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = importeStr,
                        onValueChange = { importeStr = it },
                        label = { Text("Importe (€)") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
    }
}