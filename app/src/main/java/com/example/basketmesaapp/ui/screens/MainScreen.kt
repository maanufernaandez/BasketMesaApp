package com.example.basketmesaapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.Sancion
import com.example.basketmesaapp.repository.FirestoreRepository
import com.example.basketmesaapp.ui.components.AddPartidoDialog
import com.example.basketmesaapp.ui.components.AddSancionDialog
import com.example.basketmesaapp.ui.components.PartidoCard
import com.example.basketmesaapp.ui.components.SancionCard
import com.example.basketmesaapp.utils.DataConstants
import com.example.basketmesaapp.utils.TarifaCalculator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(repository: FirestoreRepository, onLogout: () -> Unit) {
    val context = LocalContext.current
    val partidos by repository.getPartidos().collectAsState(initial = null)
    val sanciones by repository.getSanciones().collectAsState(initial = null)
    val tarifas = DataConstants.listaCategoriasFijas

    var showAddDialog by remember { mutableStateOf(false) }
    var showSancionDialog by remember { mutableStateOf(false) }
    var showStats by remember { mutableStateOf(false) }
    var partidoEnEdicion by remember { mutableStateOf<Partido?>(null) }
    var campoAEditar by remember { mutableStateOf<String?>(null) }
    var sancionEnEdicion by remember { mutableStateOf<Sancion?>(null) }
    var showProfile by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var userRol by remember { mutableStateOf("Oficial de Mesa") }
    var autorizado3Vistas by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection("usuarios").document(uid)
                .addSnapshotListener { doc, e ->
                    if (e == null && doc != null && doc.exists()) {
                        userRol = doc.getString("rol") ?: "Oficial de Mesa"
                        autorizado3Vistas = doc.getBoolean("autorizado3Vistas") ?: false
                    }
                }
        }
    }

    if (showProfile) {
        ProfileScreen(onBack = { showProfile = false }, onLogout = onLogout)
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = { Text("Partidos CNAB", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = MaterialTheme.colorScheme.onSurface) },
                    actions = {
                        IconButton(onClick = { showStats = true }) { Icon(Icons.Default.BarChart, contentDescription = "Estadísticas", tint = MaterialTheme.colorScheme.primary) }
                        IconButton(onClick = { showProfile = true }) { Icon(Icons.Default.Person, contentDescription = "Perfil", tint = MaterialTheme.colorScheme.primary) }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background, scrolledContainerColor = MaterialTheme.colorScheme.surface)
                )
            },
            floatingActionButton = {
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(50))
                            .clip(RoundedCornerShape(50))
                            .background(Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.primary, Color(0xFFFFB74D))))
                            .clickable { partidoEnEdicion = null; showAddDialog = true }
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir Partido", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("NUEVA DESIGNACIÓN", fontWeight = FontWeight.Black, fontSize = 14.sp, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .shadow(8.dp, RoundedCornerShape(50))
                            .clip(RoundedCornerShape(50))
                            .background(Brush.linearGradient(colors = listOf(Color(0xFFEF4444), Color(0xFF991B1B))))
                            .clickable { sancionEnEdicion = null; showSancionDialog = true }
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Text("— NUEVA SANCIÓN", fontWeight = FontWeight.Black, fontSize = 14.sp, letterSpacing = 1.sp, color = Color.White)
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                PartidosTab(
                    partidos = partidos,
                    sanciones = sanciones,
                    onEdit = { partido, campo -> partidoEnEdicion = partido; campoAEditar = campo; showAddDialog = true },
                    onEditSancion = { sancion -> sancionEnEdicion = sancion; showSancionDialog = true },
                    onDeletePartido = { id ->
                        scope.launch {
                            try {
                                repository.eliminarPartido(id)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error al eliminar el partido", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onDeleteSancion = { id ->
                        scope.launch {
                            try {
                                repository.eliminarSancion(id)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error al eliminar la sanción", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )

                if (showStats) {
                    EstadisticasScreen(
                        partidos = partidos ?: emptyList(),
                        sanciones = sanciones ?: emptyList(),
                        userRol = userRol,
                        onDismiss = { showStats = false }
                    )
                }

                if (showAddDialog) {
                    AddPartidoDialog(
                        categorias = tarifas, partidosExistentes = partidos ?: emptyList(), partidoAEditar = partidoEnEdicion, campoAEditar = campoAEditar, userRol = userRol, autorizado3Vistas = autorizado3Vistas,
                        onDismiss = { showAddDialog = false; partidoEnEdicion = null; campoAEditar = null },
                        onConfirm = { nuevoPartido ->
                            showAddDialog = false
                            partidoEnEdicion = null
                            scope.launch {
                                try {
                                    repository.guardarPartido(nuevoPartido.copy(totalPartido = TarifaCalculator.calcularTotal(nuevoPartido, tarifas)))
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Fallo al guardar designación", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }

                if (showSancionDialog) {
                    AddSancionDialog(
                        sancionAEditar = sancionEnEdicion,
                        onDismiss = { showSancionDialog = false; sancionEnEdicion = null },
                        onConfirm = { nuevaSancion ->
                            showSancionDialog = false
                            sancionEnEdicion = null
                            scope.launch {
                                try {
                                    repository.guardarSancion(nuevaSancion)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "No tienes permisos en Firebase para Sanciones", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PartidosTab(
    partidos: List<Partido>?,
    sanciones: List<Sancion>?,
    onEdit: (Partido, String?) -> Unit,
    onEditSancion: (Sancion) -> Unit,
    onDeletePartido: (String) -> Unit,
    onDeleteSancion: (String) -> Unit
) {
    if (partidos == null || sanciones == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
    } else if (partidos.isEmpty() && sanciones.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.surfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Aún no tienes registros", fontSize = 18.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
        }
    } else {
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
        val expandedSancionesStates = remember { androidx.compose.runtime.mutableStateMapOf<String, Boolean>() }

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp, start = 2.dp, end = 2.dp)) {
            allKeys.forEach { mesKey ->
                val mesName = getMonthName(mesKey)
                val partidosMes = partidos.filter { getMonthKey(it.fecha) == mesKey }.sortedWith(compareBy({ it.fecha }, { it.hora }))
                val sancionesMes = sanciones.filter { getMonthKey(it.fecha) == mesKey }.sortedBy { it.fecha }

                val totalPartidos = partidosMes.sumOf { it.totalPartido }
                val totalSanciones = sancionesMes.sumOf { it.importe }
                val totalNeto = totalPartidos - totalSanciones

                val isExpanded = expandedStates[mesKey] ?: true

                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable { expandedStates[mesKey] = !isExpanded },
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = "Plegar/Desplegar", tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(mesName, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                            }
                            Text(String.format(java.util.Locale.US, "%.2f €", totalNeto), fontSize = 18.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                if (isExpanded) {
                    if (sancionesMes.isNotEmpty()) {
                        item {
                            val isSancionesExp = expandedSancionesStates[mesKey] ?: false
                            Surface(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { expandedSancionesStates[mesKey] = !isSancionesExp },
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFEF4444).copy(alpha = 0.1f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.3f))
                            ) {
                                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(if (isSancionesExp) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color(0xFFEF4444))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Sanciones", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                                    }
                                    Text("-${String.format(java.util.Locale.US, "%.2f", totalSanciones)} €", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFEF4444))
                                }
                            }
                        }
                        if (expandedSancionesStates[mesKey] == true) {
                            items(sancionesMes) { sancion -> SancionCard(sancion = sancion, onEdit = onEditSancion, onDelete = onDeleteSancion) }
                        }
                    }
                    items(partidosMes) { partido -> PartidoCard(partido = partido, onEdit = onEdit, onDelete = onDeletePartido) }
                }
            }
        }
    }
}