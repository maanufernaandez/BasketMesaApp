package com.example.basketmesaapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basketmesaapp.model.CategoriaConfig
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.utils.DataConstants
import com.example.basketmesaapp.utils.TarifaCalculator
import com.example.basketmesaapp.utils.fadingEdge
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddPartidoDialog(
    categorias: List<CategoriaConfig>,
    partidosExistentes: List<Partido>,
    partidoAEditar: Partido? = null,
    campoAEditar: String? = null,
    userRol: String,
    autorizado3Vistas: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Partido) -> Unit
) {
    val pasoInicial = when (campoAEditar) {
        "Fecha" -> 1
        "Hora" -> 2
        "Oficiales", "Árbitros" -> 7
        else -> 1
    }

    var step by remember(partidoAEditar) { mutableIntStateOf(pasoInicial) }
    var fecha by remember(partidoAEditar) { mutableStateOf(partidoAEditar?.fecha ?: "") }
    var hora by remember(partidoAEditar) { mutableStateOf(partidoAEditar?.hora ?: "16:00") }
    var categoriaId by remember(partidoAEditar) { mutableStateOf(partidoAEditar?.categoriaId ?: "") }
    var polideportivo by remember(partidoAEditar) { mutableStateOf(partidoAEditar?.polideportivo ?: "") }
    var local by remember(partidoAEditar) { mutableStateOf(partidoAEditar?.equipoLocal ?: "") }
    var visitante by remember(partidoAEditar) { mutableStateOf(partidoAEditar?.equipoVisitante ?: "") }
    var numOficiales by remember(partidoAEditar) { mutableIntStateOf(partidoAEditar?.numeroOficiales ?: 2) }
    var cobraDieta by remember(partidoAEditar) { mutableStateOf(partidoAEditar?.cobraDieta ?: false) }
    val fueraPamplona by remember(partidoAEditar) { mutableStateOf(partidoAEditar?.fueraPamplona ?: false) }
    var tipoDesplazamiento by remember(partidoAEditar) { mutableStateOf(partidoAEditar?.tipoDesplazamiento ?: "Ninguno") }
    val plusDesplazamiento by remember(partidoAEditar) { mutableStateOf(if (partidoAEditar != null && partidoAEditar.plusDesplazamiento > 0.0) partidoAEditar.plusDesplazamiento.toString() else "") }

    var invertirLocalia by remember(partidoAEditar) { mutableStateOf(false) }

    fun verificarFueraDeHorario(catId: String, fechaStr: String, horaStr: String): Boolean {
        if (fechaStr.isEmpty() || horaStr.isEmpty() || !horaStr.contains(":")) return false
        val base = catId.lowercase()
        if (base.contains("seleccion")) return false

        val isSenior = base.contains("senior") || base.contains("2ª division mas") || base.contains("2ª división mas")
        val isJunior = base.contains("junior")
        val isCadete = base.contains("cadete")

        if (!isSenior && !isJunior && !isCadete) return false

        val parts = horaStr.split(":")
        val horaNum = parts[0].toIntOrNull() ?: return false
        val minNum = parts[1].toIntOrNull() ?: return false
        val mins = horaNum * 60 + minNum

        val cal = java.util.Calendar.getInstance().apply {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            time = sdf.parse(fechaStr) ?: return false
        }
        val dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK)
        val esFestivo = DataConstants.festivosTemporada.contains(fechaStr)

        return when {
            dayOfWeek == java.util.Calendar.SUNDAY || esFestivo -> {
                when {
                    isSenior || isJunior -> mins < 10 * 60 || (mins >= 12 * 60 + 30 && mins < 16 * 60) || mins > 18 * 60
                    isCadete -> mins < 10 * 60 || mins > 12 * 60 + 30
                    else -> false
                }
            }
            dayOfWeek == java.util.Calendar.SATURDAY -> {
                when {
                    isSenior -> mins < 16 * 60 || mins > 20 * 60 + 30
                    isJunior -> mins < 10 * 60 || (mins >= 13 * 60 && mins < 16 * 60) || mins > 20 * 60 + 30
                    isCadete -> mins < 9 * 60 || (mins >= 13 * 60 + 20 && mins < 16 * 60) || mins > 20 * 60 + 30
                    else -> false
                }
            }
            else -> {
                mins < 18 * 60 || mins >= 20 * 60 + 30
            }
        }
    }

    LaunchedEffect(fecha, hora, categoriaId) {
        cobraDieta = verificarFueraDeHorario(categoriaId, fecha, hora)
    }

    val teamsInCategory = remember(categoriaId) {
        fun normalize(s: String) = s.lowercase().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace("masculino", "masculin").replace("masculina", "masculin").replace("femenino", "femenin").replace("femenina", "femenin").replace(" ", "").replace("/", "").replace("-", "")
        val normalizedId = normalize(categoriaId)
        DataConstants.categoriasData.entries.find { entry ->
            val normalizedKey = normalize(entry.key)
            normalizedKey == normalizedId || normalizedId.contains(normalizedKey) || normalizedKey.contains(normalizedId)
        }?.value ?: emptyList()
    }

    val requiresOfficialSelection = remember(categoriaId, userRol) {
        fun normalize(s: String) = s.lowercase().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace("masculino", "masculin").replace("masculina", "masculin").replace("femenino", "femenin").replace("femenina", "femenin").replace(" ", "").replace("/", "").replace("-", "")
        val normalizedId = normalize(categoriaId)

        if (userRol == "Árbitro") {
            val fijasArbitro = listOf("1ªdivision", "2ªdivision", "copanavarra", "seleccion")
            !fijasArbitro.any { normalizedId.contains(it) }
        } else {
            if (normalizedId.contains("cadete")) return@remember false

            val flexibleMesa = listOf("lfchallenge", "ligaeba", "copanavarra", "2ªdivisionmasculin", "2ªdivisionfemenin", "seniormasculin1ª", "seniorfemenin1ª", "juniormasculin1ª", "juniorfemenin1ª")
            flexibleMesa.any { normalizedId.contains(it) || it.contains(normalizedId) }
        }
    }

    when (step) {
        1 -> {
            var fechaTemporal by remember { mutableStateOf(fecha) }
            BaseStepDialog(
                title = "Selecciona el día",
                onDismiss = onDismiss,
                onBack = null,
                onNext = {
                    fecha = fechaTemporal
                    if (campoAEditar != null) {
                        val p = (partidoAEditar ?: Partido()).copy(fecha = fecha, rol = userRol, autorizado3Vistas = autorizado3Vistas)
                        onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias)))
                        onDismiss()
                    } else step = 2
                },
                nextEnabled = fechaTemporal.isNotEmpty(),
                nextText = if (campoAEditar != null) "Guardar" else "Siguiente"
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CustomDatePicker(initialDate = fechaTemporal) { nuevaFecha -> fechaTemporal = nuevaFecha }
                }
            }
        }
        2 -> {
            val parsedHour = try { if (hora.isNotEmpty()) hora.split(":")[0].toInt() else 16 } catch(e: Exception) { 16 }
            val parsedMinute = try { if (hora.isNotEmpty()) hora.split(":")[1].toInt() else 0 } catch(e: Exception) { 0 }
            var horaTemporal by remember { mutableStateOf(hora) }
            BaseStepDialog(
                title = "Selecciona la hora",
                onDismiss = onDismiss,
                onBack = if (campoAEditar == null) { { step = 1 } } else null,
                onNext = {
                    val parts = horaTemporal.split(":")
                    val minNuevos = parts[0].toInt() * 60 + parts[1].toInt()
                    val conflicto = partidosExistentes.find { p ->
                        p.id != partidoAEditar?.id && p.fecha == fecha && run {
                            val minExist = try { val sp = p.hora.split(":"); sp[0].toInt() * 60 + sp[1].toInt() } catch(e: Exception) { 0 }
                            kotlin.math.abs(minExist - minNuevos) < 105
                        }
                    }
                    hora = horaTemporal
                    if (conflicto != null) { step = 99 } else {
                        val p = (partidoAEditar ?: Partido()).copy(hora = hora, rol = userRol, autorizado3Vistas = autorizado3Vistas)
                        onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias)))
                        onDismiss()
                    }
                },
                nextText = "Guardar"
            ) {
                CustomTimePicker(initialHour = parsedHour, initialMinute = parsedMinute) { timeString -> horaTemporal = timeString }
            }
        }
        99 -> {
            BaseStepDialog(
                title = "⚠️ Conflicto de Horario",
                titleColor = Color(0xFFEF4444),
                onDismiss = onDismiss,
                onBack = null,
                onNext = { step = 2 },
                nextText = "Cambiar Hora"
            ) {
                val partidoConflicto = partidosExistentes.find { p -> p.id != partidoAEditar?.id && p.fecha == fecha && run { val minNuevos = try { val pa = hora.split(":"); pa[0].toInt() * 60 + pa[1].toInt() } catch(e: Exception) { 0 }; val minExistentes = try { val sp = p.hora.split(":"); sp[0].toInt() * 60 + sp[1].toInt() } catch(e: Exception) { 0 }; kotlin.math.abs(minExistentes - minNuevos) < 105 } }
                Text("Tienes otro partido a las ${partidoConflicto?.hora} que impide esta designación.", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
            }
        }
        3 -> {
            val listState = rememberLazyListState()
            BaseStepDialog(title = "Categoría", onDismiss = onDismiss, onBack = { step = 2 }, onNext = null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        modifier = Modifier.fillMaxSize().fadingEdge(listState)
                    ) {
                        items(categorias) { cat ->
                            TextButton(
                                onClick = {
                                    if (cat.id == "Selección Navarra") {
                                        categoriaId = cat.id
                                        step = 31
                                    } else {
                                        categoriaId = cat.id

                                        numOficiales = if (userRol == "Árbitro") 2 else {
                                            val catLower = cat.id.lowercase()
                                            when {
                                                catLower.contains("lf") || catLower.contains("eba") -> 3
                                                catLower.contains("copa") -> 2
                                                catLower.contains("cadete") -> 1
                                                catLower.contains("1ª division") || catLower.contains("1ª división") || catLower.contains("2ª division") || catLower.contains("2ª división") -> 2
                                                catLower.contains("senior") && catLower.contains("1ª") -> 2
                                                catLower.contains("junior") && catLower.contains("1ª") -> 2
                                                else -> 1
                                            }
                                        }

                                        if (campoAEditar != null) {
                                            val p = (partidoAEditar ?: Partido()).copy(categoriaId = categoriaId, numeroOficiales = numOficiales, rol = userRol, autorizado3Vistas = autorizado3Vistas)
                                            onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias)))
                                            onDismiss()
                                        } else step = 4
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text(cat.id, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        }
                    }
                }
            }
        }
        31 -> {
            val subcats = listOf("Junior Masculino", "Junior Femenino", "Cadete Masculino", "Cadete Femenino", "Infantil Masculino", "Infantil Femenino", "Mini Masculino", "Mini Femenino")
            val listState = rememberLazyListState()
            BaseStepDialog(title = "Selección Navarra", onDismiss = onDismiss, onBack = { step = 3 }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        modifier = Modifier.fillMaxSize().fadingEdge(listState)
                    ) {
                        items(subcats) { sub ->
                            TextButton(
                                onClick = {
                                    categoriaId = "Selección Navarra $sub"
                                    numOficiales = if (userRol == "Árbitro") 2 else 1
                                    local = ""
                                    visitante = ""
                                    step = 32
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
            BaseStepDialog(title = "Polideportivo", onDismiss = onDismiss, onBack = { step = 31 }) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(onClick = { polideportivo = "Larrabide"; step = 8 }, modifier = Modifier.fillMaxWidth()) { Text("Larrabide", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                    HorizontalDivider()
                    TextButton(onClick = { polideportivo = ""; step = 33 }, modifier = Modifier.fillMaxWidth()) { Text("Otros", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                }
            }
        }
        33 -> {
            BaseStepDialog(title = "Escribe el Polideportivo", onDismiss = onDismiss, onBack = { step = 32 }, onNext = { step = 8 }, nextEnabled = polideportivo.isNotBlank()) {
                OutlinedTextField(value = polideportivo, onValueChange = { polideportivo = it }, label = { Text("Nombre del pabellón") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            }
        }
        4 -> {
            val listState = rememberLazyListState()
            val availablePolis = teamsInCategory.flatMap { it.polideportivos }.distinct().sorted()
            val displayPolis = availablePolis + "Otro"
            BaseStepDialog(title = "Polideportivo", onDismiss = onDismiss, onBack = { step = 3 }, onNext = null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        modifier = Modifier.fillMaxSize().fadingEdge(listState)
                    ) {
                        items(displayPolis) { poli ->
                            TextButton(
                                onClick = {
                                    if (poli == "Otro") { polideportivo = ""; step = 45 }
                                    else {
                                        polideportivo = poli
                                        if (campoAEditar != null) {
                                            val p = (partidoAEditar ?: Partido()).copy(polideportivo = polideportivo, rol = userRol, autorizado3Vistas = autorizado3Vistas)
                                            onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias))); onDismiss()
                                        } else {
                                            val localCandidates = teamsInCategory.filter { it.polideportivos.contains(poli) }
                                            if (localCandidates.size == 1) { local = localCandidates[0].nombre; step = 6 } else step = 5
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
            BaseStepDialog(title = "Polideportivo", onDismiss = onDismiss, onBack = { step = 4 }, onNext = { step = 5 }, nextEnabled = polideportivo.isNotBlank()) {
                OutlinedTextField(value = polideportivo, onValueChange = { polideportivo = it }, label = { Text("Escribe el nombre del polideportivo") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            }
        }
        5 -> {
            val listState = rememberLazyListState()
            val availablePolis = teamsInCategory.flatMap { it.polideportivos }.distinct()
            val localCandidates = if (availablePolis.contains(polideportivo)) teamsInCategory.filter { it.polideportivos.contains(polideportivo) } else teamsInCategory
            val filteredCandidates = localCandidates.filter { it.nombre != "Visitante" }
            BaseStepDialog(title = "Equipo Local", onDismiss = onDismiss, onBack = { step = if (availablePolis.contains(polideportivo)) 4 else 45 }) {
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(vertical = 16.dp),
                        modifier = Modifier.fillMaxSize().fadingEdge(listState)
                    ) {
                        items(filteredCandidates) { team ->
                            TextButton(onClick = { local = team.nombre; step = 6 }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) { Text(team.nombre, fontSize = 17.sp) }
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        }
                    }
                }
            }
        }
        6 -> {
            val listState = rememberLazyListState()
            val availablePolis = teamsInCategory.flatMap { it.polideportivos }.distinct()
            val visitorCandidates = teamsInCategory.filter { it.nombre != local }
            BaseStepDialog(title = "Equipo Visitante", onDismiss = onDismiss, onBack = { val localCandidates = teamsInCategory.filter { it.polideportivos.contains(polideportivo) }; step = if (localCandidates.size == 1 && availablePolis.contains(polideportivo)) 4 else 5 }) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp).clickable { invertirLocalia = !invertirLocalia }) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) {
                            Checkbox(checked = invertirLocalia, onCheckedChange = { invertirLocalia = it })
                            Text(text = "El equipo visitante actuará como LOCAL", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
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
                                        visitante = team.nombre
                                        if (campoAEditar != null) {
                                            val finalLocal = if (invertirLocalia) visitante else local
                                            val finalVisitante = if (invertirLocalia) local else visitante
                                            val p = (partidoAEditar ?: Partido()).copy(equipoLocal = finalLocal, equipoVisitante = finalVisitante, rol = userRol, autorizado3Vistas = autorizado3Vistas)
                                            onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias))); onDismiss()
                                        } else step = if (requiresOfficialSelection) 7 else 8
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
        7 -> {
            val opcionesOficiales = if (userRol == "Árbitro") {
                val catLower = categoriaId.lowercase()
                if (catLower.contains("1ª division") || catLower.contains("1ª división") ||
                    catLower.contains("2ª division") || catLower.contains("2ª división") ||
                    catLower.contains("copa navarra") || catLower.contains("selección") || catLower.contains("seleccion")
                ) {
                    listOf(2)
                } else {
                    listOf(1, 2)
                }
            } else {
                val catLower = categoriaId.lowercase().replace("á", "a").replace("é", "e").replace("í", "i").replace("ó", "o").replace("ú", "u").replace("femenino", "femenin").replace("masculino", "masculin")
                when {
                    catLower.contains("lfchallenge") || catLower.contains("ligaeba") -> listOf(3, 4)
                    catLower.contains("copanavarra") -> listOf(2, 3)
                    catLower.contains("cadete") -> listOf(1)
                    else -> {
                        if (autorizado3Vistas) {
                            listOf(1, 2)
                        } else {
                            val permiteUno = catLower.contains("2ª division femenin") ||
                                    catLower.contains("2ª division masculin") ||
                                    (catLower.contains("senior") && catLower.contains("1ª")) ||
                                    (catLower.contains("junior") && catLower.contains("1ª"))

                            if (permiteUno) listOf(1, 2) else listOf(2)
                        }
                    }
                }
            }

            val pueblos = mapOf("Alsasua" to "Alsasua", "Estella" to "Estella", "Tudela" to "Tudela", "Puente" to "Puente la reina", "San Adrián" to "San Adrián", "Tafalla" to "Tafalla", "Sangüesa" to "Sangüesa", "Peralta" to "Peralta")
            val esDesplazamiento = pueblos.keys.any { polideportivo.contains(it, ignoreCase = true) }

            if (!opcionesOficiales.contains(numOficiales)) {
                numOficiales = opcionesOficiales.first()
            }

            BaseStepDialog(
                title = if (userRol == "Árbitro") "Árbitros" else "Oficiales",
                onDismiss = onDismiss,
                onBack = { step = 6 },
                onNext = {
                    if (campoAEditar != null) {
                        if (esDesplazamiento) {
                            step = 8
                        } else {
                            val p = (partidoAEditar ?: Partido()).copy(numeroOficiales = numOficiales, voySolo = (numOficiales == 1), rol = userRol, autorizado3Vistas = autorizado3Vistas)
                            onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias)))
                            onDismiss()
                        }
                    } else {
                        step = 8
                    }
                },
                nextText = if (campoAEditar != null && !esDesplazamiento) "Guardar" else "Siguiente"
            ) {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    opcionesOficiales.forEach { num ->
                        val isSelected = numOficiales == num
                        Surface(
                            modifier = Modifier.padding(horizontal = 12.dp).size(75.dp).clickable { numOficiales = num },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) Color(0xFFF97316) else MaterialTheme.colorScheme.surfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(2.dp, if (isSelected) Color(0xFFF97316) else Color.Gray.copy(alpha = 0.3f))
                        ) { Box(contentAlignment = Alignment.Center) { Text(text = num.toString(), fontSize = 28.sp, fontWeight = FontWeight.Black, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface) } }
                    }
                }
            }
        }
        8 -> {
            val pueblos = mapOf("Alsasua" to "Alsasua", "Estella" to "Estella", "Tudela" to "Tudela", "Puente" to "Puente la reina", "San Adrián" to "San Adrián", "Tafalla" to "Tafalla", "Sangüesa" to "Sangüesa", "Peralta" to "Peralta")
            val esDesplazamiento = pueblos.keys.any { polideportivo.contains(it, ignoreCase = true) }
            LaunchedEffect(Unit) { if (!esDesplazamiento && campoAEditar == null) step = 9 }

            BaseStepDialog(
                title = "Extras",
                onDismiss = onDismiss,
                onBack = { step = if (categoriaId.contains("Selección Navarra")) (if (polideportivo == "Larrabide") 32 else 33) else if (requiresOfficialSelection) 7 else 6 },
                onNext = {
                    if (campoAEditar != null) {
                        val p = (partidoAEditar ?: Partido()).copy(numeroOficiales = numOficiales, voySolo = (numOficiales == 1), tipoDesplazamiento = tipoDesplazamiento, rol = userRol, autorizado3Vistas = autorizado3Vistas)
                        onConfirm(p.copy(totalPartido = TarifaCalculator.calcularTotal(p, categorias)))
                        onDismiss()
                    } else {
                        step = 9
                    }
                },
                nextText = if (campoAEditar != null) "Guardar" else "Siguiente"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (esDesplazamiento) {
                        Text("Transporte:", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { tipoDesplazamiento = "Conductor" }) { RadioButton(selected = tipoDesplazamiento == "Conductor", onClick = { tipoDesplazamiento = "Conductor" }); Text("Llevo coche (Conductor)") }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().clickable { tipoDesplazamiento = "Acompañante" }) { RadioButton(selected = tipoDesplazamiento == "Acompañante", onClick = { tipoDesplazamiento = "Acompañante" }); Text("Voy de acompañante") }
                    }
                }
            }
        }
        9 -> {
            val localeSpanish = java.util.Locale("es", "ES")
            val fechaFormateada = try { val dateObj = java.text.SimpleDateFormat("yyyy-MM-dd", localeSpanish).parse(fecha); val formatDia = java.text.SimpleDateFormat("EEEE d", localeSpanish).format(dateObj!!); formatDia.replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeSpanish) else it.toString() } } catch (e: Exception) { fecha }
            val pueblos = mapOf("Alsasua" to "Alsasua", "Estella" to "Estella", "Tudela" to "Tudela", "Puente" to "Puente la reina", "San Adrián" to "San Adrián", "Tafalla" to "Tafalla", "Sangüesa" to "Sangüesa", "Peralta" to "Peralta")
            val esDesplazamiento = pueblos.keys.any { polideportivo.contains(it, ignoreCase = true) }
            val seSaltoPaso8 = !esDesplazamiento

            // MODIFICACIÓN: Aquí hacemos que si es obligatorio ir 1 oficial de mesa, se oculte el texto por completo.
            val textoOficiales = if (userRol == "Árbitro") {
                if (numOficiales == 1) "1 árbitro" else "$numOficiales árbitros"
            } else {
                if (numOficiales == 1) {
                    if (requiresOfficialSelection) {
                        if (autorizado3Vistas) "3 funciones vistas" else "1 oficial"
                    } else {
                        "" // Se oculta en los partidos donde siempre va 1 oficial de mesa
                    }
                } else {
                    "$numOficiales oficiales"
                }
            }

            val finalLocal = if (invertirLocalia) visitante else local
            val finalVisitante = if (invertirLocalia) local else visitante

            val formatTitleCase = { text: String ->
                text.lowercase().split(" ").joinToString(" ") { word ->
                    word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeSpanish) else it.toString() }
                }
            }

            val categoriaFormateada = if (categoriaId.startsWith("Selección Navarra", ignoreCase = true)) {
                formatTitleCase(categoriaId).replace("Masculino", "Masc.").replace("Femenino", "Fem.")
            } else {
                formatTitleCase(categoriaId)
            }

            BaseStepDialog(
                title = "Resumen del Partido",
                onDismiss = onDismiss,
                onBack = { step = if (categoriaId.contains("Selección Navarra")) (if (polideportivo == "Larrabide") 32 else 33) else if (seSaltoPaso8) (if (requiresOfficialSelection) 7 else 6) else 8 },
                onNext = {
                    val partidoGenerado = Partido(fecha = fecha, hora = hora, polideportivo = polideportivo, categoriaId = categoriaId, equipoLocal = finalLocal, equipoVisitante = finalVisitante, numeroOficiales = numOficiales, cobraDieta = cobraDieta, fueraPamplona = fueraPamplona, tipoDesplazamiento = tipoDesplazamiento, plusDesplazamiento = plusDesplazamiento.toDoubleOrNull() ?: 0.0, userId = FirebaseAuth.getInstance().currentUser?.uid ?: "", rol = userRol, autorizado3Vistas = autorizado3Vistas)
                    val partidoFinal = if (partidoAEditar != null) partidoGenerado.copy(id = partidoAEditar.id) else partidoGenerado
                    onConfirm(partidoFinal.copy(totalPartido = TarifaCalculator.calcularTotal(partidoFinal, categorias)))
                    onDismiss()
                },
                nextText = "Guardar"
            ) {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.padding(20.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) { Text("🗓️", fontSize = 20.sp) }
                            Spacer(Modifier.width(16.dp))
                            Text("$fechaFormateada - $hora", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) { Text("🏆", fontSize = 20.sp) }
                            Spacer(Modifier.width(16.dp))
                            Text(categoriaFormateada, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }

                        // MODIFICACIÓN: Si textoOficiales está vacío, ni siquiera dibujamos la fila
                        if (textoOficiales.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) { Text("👥", fontSize = 20.sp) }
                                Spacer(Modifier.width(16.dp))
                                Text(textoOficiales, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            }
                        }

                        if (tipoDesplazamiento != "Ninguno") {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) { Text("🚗", fontSize = 20.sp) }
                                Spacer(Modifier.width(16.dp))
                                Text(tipoDesplazamiento, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) { Text("🏟️", fontSize = 20.sp) }
                            Spacer(Modifier.width(16.dp))
                            Text("Pdvo. $polideportivo", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                        if (finalLocal.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) { Text("A", fontSize = 20.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary) }
                                Spacer(Modifier.width(16.dp))
                                Text(finalLocal, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                        if (finalVisitante.isNotEmpty()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) { Text("B", fontSize = 20.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary) }
                                Spacer(Modifier.width(16.dp))
                                Text(finalVisitante, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }
    }
}