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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.Sancion
import com.example.basketmesaapp.model.datosOrNull
import com.example.basketmesaapp.model.errorOrNull
import com.example.basketmesaapp.ui.components.AddPartidoDialog
import com.example.basketmesaapp.ui.components.AddSancionDialog
import com.example.basketmesaapp.ui.components.PartidoCard
import com.example.basketmesaapp.ui.components.SancionCard
import com.example.basketmesaapp.utils.AgrupacionMensual
import com.example.basketmesaapp.utils.DataConstants
import com.example.basketmesaapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel, onLogout: () -> Unit) {
    val context = LocalContext.current

    val partidosState by viewModel.partidos.collectAsState()
    val sancionesState by viewModel.sanciones.collectAsState()
    val partidos = partidosState.datosOrNull()
    val sanciones = sancionesState.datosOrNull()
    val errorCarga = partidosState.errorOrNull() ?: sancionesState.errorOrNull()
    val tarifas = DataConstants.listaCategoriasFijas
    val userRol by viewModel.userRol.collectAsState()
    val autorizado3Vistas by viewModel.autorizado3Vistas.collectAsState()
    val equiposRemotos by viewModel.equiposRemotos.collectAsState()
    val festivosRemotos by viewModel.festivosRemotos.collectAsState()

    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var showSancionDialog by rememberSaveable { mutableStateOf(false) }
    var showStats by rememberSaveable { mutableStateOf(false) }
    var showProfile by rememberSaveable { mutableStateOf(false) }
    var campoAEditar by rememberSaveable { mutableStateOf<String?>(null) }
    var partidoEnEdicionId by rememberSaveable { mutableStateOf<String?>(null) }
    var sancionEnEdicionId by rememberSaveable { mutableStateOf<String?>(null) }
    val partidoEnEdicion = partidoEnEdicionId?.let { id -> partidos?.firstOrNull { it.id == id } }
    val sancionEnEdicion = sancionEnEdicionId?.let { id -> sanciones?.firstOrNull { it.id == id } }

    val cerrarDialogoPartido = { showAddDialog = false; partidoEnEdicionId = null; campoAEditar = null }
    val cerrarDialogoSancion = { showSancionDialog = false; sancionEnEdicionId = null }

    val expandedStates = remember { androidx.compose.runtime.mutableStateMapOf<String, Boolean>() }
    val expandedSancionesStates = remember { androidx.compose.runtime.mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(Unit) {
        viewModel.errores.collect { mensaje ->
            Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
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
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                // --- BLOQUE DE BOTONES SUPERIORES ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón Nueva Designación (Mitad Izquierda)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .shadow(6.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.primary, Color(0xFFFFB74D))))
                            .clickable { partidoEnEdicionId = null; campoAEditar = null; showAddDialog = true }                            // Padding reducido a 8.dp para hacerlo más bajo
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                    ) {
                        // Icono más grande (28.dp)
                        Icon(Icons.Default.Add, contentDescription = "Añadir Partido", tint = Color.White, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(2.dp))
                        // Texto más grande (13.sp)
                        Text(
                            text = "NUEVA DESIGNACIÓN",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            lineHeight = 14.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Botón Nueva Sanción (Mitad Derecha)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .shadow(6.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.linearGradient(colors = listOf(Color(0xFFEF4444), Color(0xFF991B1B))))
                            .clickable { sancionEnEdicionId = null; showSancionDialog = true }                            // Padding reducido a 8.dp para hacerlo más bajo
                            .padding(vertical = 8.dp, horizontal = 4.dp)
                    ) {
                        // Icono más grande (28.dp)
                        Icon(Icons.Default.Add, contentDescription = "Añadir Sanción", tint = Color.White, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.height(2.dp))
                        // Texto más grande (13.sp)
                        Text(
                            text = "NUEVA SANCIÓN",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            lineHeight = 14.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // --- LISTA DE PARTIDOS ---
                Box(modifier = Modifier.weight(1f)) {
                    PartidosTab(
                        partidos = partidos,
                        sanciones = sanciones,
                        errorCarga = errorCarga,
                        onReintentar = { viewModel.reintentarCarga() },
                        expandedStates = expandedStates,
                        expandedSancionesStates = expandedSancionesStates,
                        onEdit = { partido, campo -> partidoEnEdicionId = partido.id; campoAEditar = campo; showAddDialog = true },
                        onEditSancion = { sancion -> sancionEnEdicionId = sancion.id; showSancionDialog = true },
                        onDeletePartido = { id -> viewModel.eliminarPartido(id) },
                        onDeleteSancion = { id -> viewModel.eliminarSancion(id) }
                    )
                }
            }

            // --- DIÁLOGOS ---
            if (showStats) {
                EstadisticasScreen(
                    partidos = partidos ?: emptyList(),
                    sanciones = sanciones ?: emptyList(),
                    userRol = userRol,
                    onDismiss = { showStats = false }
                )
            }

            // Si se estaba editando un partido y aún no se ha cargado la lista
            // (p. ej. tras recrearse la pantalla), se espera antes de abrir el diálogo.
            if (showAddDialog && (partidoEnEdicionId == null || partidoEnEdicion != null)) {
                AddPartidoDialog(
                    categorias = tarifas, partidosExistentes = partidos ?: emptyList(), partidoAEditar = partidoEnEdicion, campoAEditar = campoAEditar, userRol = userRol, autorizado3Vistas = autorizado3Vistas,
                    equiposRemotos = equiposRemotos,
                    festivosRemotos = festivosRemotos.map { it.fecha },
                    onDismiss = cerrarDialogoPartido,
                    onConfirm = { nuevoPartido ->
                        cerrarDialogoPartido()
                        viewModel.guardarPartido(nuevoPartido)
                    }
                )
            }

            if (showSancionDialog && (sancionEnEdicionId == null || sancionEnEdicion != null)) {
                AddSancionDialog(
                    sancionAEditar = sancionEnEdicion,
                    festivosRemotos = festivosRemotos.map { it.fecha },
                    onDismiss = cerrarDialogoSancion,
                    onConfirm = { nuevaSancion ->
                        cerrarDialogoSancion()
                        viewModel.guardarSancion(nuevaSancion)
                    }
                )
            }
        }
    }
}

@Composable
fun PartidosTab(
    partidos: List<Partido>?,
    sanciones: List<Sancion>?,
    errorCarga: String?,
    onReintentar: () -> Unit,
    expandedStates: androidx.compose.runtime.snapshots.SnapshotStateMap<String, Boolean>,
    expandedSancionesStates: androidx.compose.runtime.snapshots.SnapshotStateMap<String, Boolean>,
    onEdit: (Partido, String?) -> Unit,
    onEditSancion: (Sancion) -> Unit,
    onDeletePartido: (String) -> Unit,
    onDeleteSancion: (String) -> Unit
) {
    if (errorCarga != null) {
        Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Text(errorCarga, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onReintentar) { Text("Reintentar") }
            }
        }
    } else if (partidos == null || sanciones == null) {
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
        // Se agrupa una sola vez por cada cambio de datos, no en cada recomposición.
        val meses = remember(partidos, sanciones) { AgrupacionMensual.agrupar(partidos, sanciones) }

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp, start = 2.dp, end = 2.dp)) {
            meses.forEach { mes ->
                val mesKey = mes.clave
                val isExpanded = expandedStates[mesKey] ?: true

                item(key = "mes_$mesKey") {
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
                                Text(mes.nombre, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                            }
                            Text(String.format(java.util.Locale.US, "%.2f €", mes.totalNeto), fontSize = 18.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                if (isExpanded) {
                    if (mes.sanciones.isNotEmpty()) {
                        item(key = "sanciones_$mesKey") {
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
                                    Text("-${String.format(java.util.Locale.US, "%.2f", mes.totalSanciones)} €", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFEF4444))
                                }
                            }
                        }
                        if (expandedSancionesStates[mesKey] == true) {
                            items(mes.sanciones, key = { "sancion_${it.id}" }) { sancion ->
                                SancionCard(sancion = sancion, onEdit = onEditSancion, onDelete = onDeleteSancion)
                            }
                        }
                    }
                    items(mes.partidos, key = { "partido_${it.id}" }) { partido ->
                        PartidoCard(partido = partido, onEdit = onEdit, onDelete = onDeletePartido)
                    }
                }
            }
        }
    }
}