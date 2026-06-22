package com.example.basketmesaapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basketmesaapp.model.Sancion

@Composable
fun SancionCard(sancion: Sancion, onEdit: (Sancion) -> Unit, onDelete: (String) -> Unit) {
    val localeSpanish = java.util.Locale("es", "ES")
    val formatPrice = { price: Double -> String.format(java.util.Locale.US, "%.2f€", price) }

    val fechaFormateada = try {
        val dateObj = java.text.SimpleDateFormat("yyyy-MM-dd", localeSpanish).parse(sancion.fecha)
        val formatDia = java.text.SimpleDateFormat("EEEE d", localeSpanish).format(dateObj!!)
        formatDia.replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeSpanish) else it.toString() }
    } catch (e: Exception) { sancion.fecha }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp, vertical = 6.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFEF4444).copy(alpha = 0.15f),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "SANCIÓN",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = Color(0xFFEF4444)
                    )
                }
                Text(
                    text = "-${formatPrice(sancion.importe)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFFEF4444)
                )
            }

            Text(
                text = sancion.motivo,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = fechaFormateada,
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { onEdit(sancion) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                }
                IconButton(onClick = { onDelete(sancion.id) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color(0xFFEF4444).copy(alpha = 0.8f))
                }
            }
        }
    }
}