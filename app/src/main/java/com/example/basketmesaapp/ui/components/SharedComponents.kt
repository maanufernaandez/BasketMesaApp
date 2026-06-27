package com.example.basketmesaapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.basketmesaapp.utils.DataConstants

@Composable
fun BaseStepDialog(
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    onDismiss: () -> Unit,
    onBack: (() -> Unit)? = null,
    onNext: (() -> Unit)? = null,
    nextText: String = "Siguiente",
    nextEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.80f)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = titleColor)
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEF4444),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Cancelar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            maxLines = 1
                        )
                    }

                    if (onBack != null) {
                        Button(
                            onClick = onBack,
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 2.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF64748B),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Atrás",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }

                    if (onNext != null) {
                        Button(
                            onClick = onNext,
                            enabled = nextEnabled,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            contentPadding = PaddingValues(horizontal = 2.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (nextText == "Guardar") Color(0xFF4ADE80) else MaterialTheme.colorScheme.primary,
                                contentColor = Color(0xFF111827),
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                disabledContentColor = Color.LightGray
                            )
                        ) {
                            Text(
                                text = nextText,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CustomTimePicker(initialHour: Int, initialMinute: Int, onTimeSelected: (String) -> Unit) {
    val hours = (8..22).toList()
    val minutes = (0..55 step 5).toList()
    val orangeColor = Color(0xFFFF8A65)

    val hourOffset = hours.indexOf(initialHour).let { if (it == -1) hours.indexOf(16) else it }
    val minuteOffset = minutes.indexOf(initialMinute).let { if (it == -1) 0 else it }

    val targetHourIndex = 500 - (500 % hours.size) + hourOffset
    val targetMinuteIndex = 500 - (500 % minutes.size) + minuteOffset

    val hourListState = remember(initialHour) { LazyListState(firstVisibleItemIndex = targetHourIndex - 2) }
    val minuteListState = remember(initialMinute) { LazyListState(firstVisibleItemIndex = targetMinuteIndex - 2) }

    val hourSnap = rememberSnapFlingBehavior(lazyListState = hourListState)
    val minuteSnap = rememberSnapFlingBehavior(lazyListState = minuteListState)

    fun getCenterIndex(state: LazyListState): Int {
        val layoutInfo = state.layoutInfo
        val center = layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2
        return layoutInfo.visibleItemsInfo.firstOrNull { it.offset <= center && it.offset + it.size >= center }?.index ?: 0
    }

    val selectedHourIndex by remember { derivedStateOf { getCenterIndex(hourListState) } }
    val selectedMinuteIndex by remember { derivedStateOf { getCenterIndex(minuteListState) } }

    LaunchedEffect(selectedHourIndex, selectedMinuteIndex) {
        val h = hours[selectedHourIndex % hours.size]
        val m = minutes[selectedMinuteIndex % minutes.size]
        onTimeSelected(String.format("%02d:%02d", h, m))
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(300.dp), contentAlignment = Alignment.Center) {
            Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {

                // Horas
                Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                    LazyColumn(
                        state = hourListState,
                        flingBehavior = hourSnap,
                        modifier = Modifier.width(70.dp).fillMaxHeight()
                    ) {
                        items(1000) { index ->
                            val hour = hours[index % hours.size]
                            val isSelected = index == selectedHourIndex
                            Box(
                                modifier = Modifier
                                    .height(60.dp)
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        val layoutInfo = hourListState.layoutInfo
                                        val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }
                                        if (itemInfo != null) {
                                            val viewportCenter = layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2f
                                            val itemCenter = itemInfo.offset + (itemInfo.size / 2f)
                                            val distance = kotlin.math.abs(viewportCenter - itemCenter)
                                            val maxDistance = (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2f
                                            val fraction = 1f - (distance / maxDistance).coerceIn(0f, 1f)
                                            alpha = 0.15f + 0.85f * fraction
                                            val sign = if (itemCenter > viewportCenter) -1f else 1f
                                            rotationX = sign * (distance / maxDistance) * 60f
                                            val scale = 0.75f + 0.25f * fraction
                                            scaleX = scale
                                            scaleY = scale
                                        } else {
                                            alpha = 0f
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = String.format("%02d", hour),
                                    fontSize = if (isSelected) 34.sp else 26.sp,
                                    color = if (isSelected) orangeColor else Color.Gray,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Separador ":"
                Box(
                    modifier = Modifier.wrapContentWidth().fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ":",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                // Minutos
                Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                    LazyColumn(
                        state = minuteListState,
                        flingBehavior = minuteSnap,
                        modifier = Modifier.width(70.dp).fillMaxHeight()
                    ) {
                        items(1000) { index ->
                            val minute = minutes[index % minutes.size]
                            val isSelected = index == selectedMinuteIndex
                            Box(
                                modifier = Modifier
                                    .height(60.dp)
                                    .fillMaxWidth()
                                    .graphicsLayer {
                                        val layoutInfo = minuteListState.layoutInfo
                                        val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }
                                        if (itemInfo != null) {
                                            val viewportCenter = layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2f
                                            val itemCenter = itemInfo.offset + (itemInfo.size / 2f)
                                            val distance = kotlin.math.abs(viewportCenter - itemCenter)
                                            val maxDistance = (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2f
                                            val fraction = 1f - (distance / maxDistance).coerceIn(0f, 1f)
                                            alpha = 0.15f + 0.85f * fraction
                                            val sign = if (itemCenter > viewportCenter) -1f else 1f
                                            rotationX = sign * (distance / maxDistance) * 60f
                                            val scale = 0.75f + 0.25f * fraction
                                            scaleX = scale
                                            scaleY = scale
                                        } else {
                                            alpha = 0f
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = String.format("%02d", minute),
                                    fontSize = if (isSelected) 34.sp else 26.sp,
                                    color = if (isSelected) orangeColor else Color.Gray,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDatePicker(
    initialDate: String,
    temporadaInicio: String = DataConstants.TEMPORADAINICIO,
    temporadaFin: String = DataConstants.TEMPORADAFIN,
    festivos: List<String> = DataConstants.festivosTemporada,
    onDateSelected: (String) -> Unit
) {
    val localeSpanish = java.util.Locale("es", "ES")

    val startLimit = remember(temporadaInicio) {
        java.util.Calendar.getInstance(localeSpanish).apply {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            time = sdf.parse(temporadaInicio) ?: time
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
    }
    val endLimit = remember(temporadaFin) {
        java.util.Calendar.getInstance(localeSpanish).apply {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            time = sdf.parse(temporadaFin) ?: time
            set(java.util.Calendar.HOUR_OF_DAY, 23)
            set(java.util.Calendar.MINUTE, 59)
            set(java.util.Calendar.SECOND, 59)
            set(java.util.Calendar.MILLISECOND, 999)
        }
    }

    var selectedDateStr by remember(initialDate) {
        mutableStateOf(
            initialDate.ifEmpty {
                val now = java.util.Calendar.getInstance(localeSpanish)
                val target = if (now.before(startLimit) || now.after(endLimit))
                    startLimit.clone() as java.util.Calendar
                else
                    now.clone() as java.util.Calendar

                if (target.get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.THURSDAY) {
                    target.add(java.util.Calendar.DAY_OF_MONTH, 1)
                }

                String.format(
                    java.util.Locale.US, "%04d-%02d-%02d",
                    target.get(java.util.Calendar.YEAR),
                    target.get(java.util.Calendar.MONTH) + 1,
                    target.get(java.util.Calendar.DAY_OF_MONTH)
                )
            }
        )
    }

    var currentMonth by remember(selectedDateStr) {
        mutableStateOf(
            java.util.Calendar.getInstance(localeSpanish).apply {
                val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                time = sdf.parse(selectedDateStr) ?: startLimit.time
                set(java.util.Calendar.DAY_OF_MONTH, 1)
            }
        )
    }

    LaunchedEffect(selectedDateStr) {
        onDateSelected(selectedDateStr)
    }

    val monthFormat = java.text.SimpleDateFormat("MMMM yyyy", localeSpanish)
    val monthName = monthFormat.format(currentMonth.time)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeSpanish) else it.toString() }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                val prev = currentMonth.clone() as java.util.Calendar
                prev.add(java.util.Calendar.MONTH, -1)
                currentMonth = prev
            }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Anterior", tint = MaterialTheme.colorScheme.primary)
            }
            Text(monthName, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)
            IconButton(onClick = {
                val next = currentMonth.clone() as java.util.Calendar
                next.add(java.util.Calendar.MONTH, 1)
                currentMonth = next
            }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Siguiente", tint = MaterialTheme.colorScheme.primary)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.5f))
                .padding(1.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                val daysOfWeek = listOf("L", "M", "X", "J", "V", "S", "D")
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                    daysOfWeek.forEach { day ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(day, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary, fontSize = 15.sp)
                        }
                    }
                }

                val cal = currentMonth.clone() as java.util.Calendar
                cal.set(java.util.Calendar.DAY_OF_MONTH, 1)
                var firstDayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK) - 2
                if (firstDayOfWeek < 0) firstDayOfWeek += 7

                val maxDays = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
                val totalCells = ((firstDayOfWeek + maxDays) / 7 + if ((firstDayOfWeek + maxDays) % 7 == 0) 0 else 1) * 7

                val currentYear = cal.get(java.util.Calendar.YEAR)
                val currentMonthNum = cal.get(java.util.Calendar.MONTH) + 1

                var dayCounter = 1
                for (row in 0 until (totalCells / 7)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                        for (col in 0..6) {
                            val isCurrentMonth = (row == 0 && col >= firstDayOfWeek) || (row > 0 && dayCounter <= maxDays)
                            var isSelectable = false
                            var thisDateStr = ""

                            if (isCurrentMonth) {
                                thisDateStr = String.format(
                                    java.util.Locale.US, "%04d-%02d-%02d",
                                    currentYear, currentMonthNum, dayCounter
                                )
                                val cellDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).parse(thisDateStr)
                                if (cellDate != null) {
                                    val cellCal = java.util.Calendar.getInstance().apply { time = cellDate }
                                    val isJueves = cellCal.get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.THURSDAY
                                    val isFestivo = festivos.contains(thisDateStr)
                                    isSelectable = !cellCal.before(startLimit) && !cellCal.after(endLimit) && !isJueves && !isFestivo
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .background(MaterialTheme.colorScheme.background)
                                    .clickable(enabled = isSelectable) { selectedDateStr = thisDateStr },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isCurrentMonth) {
                                    val isSelected = thisDateStr == selectedDateStr
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .background(Color(0xFFF97316), shape = RoundedCornerShape(50)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(dayCounter.toString(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        }
                                    } else {
                                        Text(
                                            text = dayCounter.toString(),
                                            color = if (isSelectable) MaterialTheme.colorScheme.onSurface else Color.Gray.copy(alpha = 0.3f),
                                            fontSize = 16.sp,
                                            fontWeight = if (isSelectable) FontWeight.SemiBold else FontWeight.Normal
                                        )
                                    }
                                    dayCounter++
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}