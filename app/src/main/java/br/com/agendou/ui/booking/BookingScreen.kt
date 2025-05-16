package br.com.agendou.ui.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

data class TimeSlot(
    val time: LocalTime,
    val available: Boolean = true
)

@Composable
fun DateItem(
    date: LocalDate,
    selected: Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("pt", "BR"))
    val dayOfMonth = date.dayOfMonth.toString()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable { onDateSelected(date) }
    ) {
        Text(
            text = dayOfWeek,
            style = MaterialTheme.typography.bodySmall,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                    shape = CircleShape
                )
                .run {
                    if (!selected) 
                        this.background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        ) 
                    else 
                        this
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfMonth,
                style = MaterialTheme.typography.bodyMedium,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun TimeSlotItem(
    timeSlot: TimeSlot,
    selected: Boolean,
    onTimeSelected: (TimeSlot) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val timeText = timeSlot.time.format(formatter)
    
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(enabled = timeSlot.available) { onTimeSelected(timeSlot) },
        colors = CardDefaults.cardColors(
            containerColor = when {
                !timeSlot.available -> MaterialTheme.colorScheme.surfaceVariant
                selected -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = when {
                !timeSlot.available -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                selected -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            }
        )
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = timeText,
                style = MaterialTheme.typography.bodyMedium,
                color = when {
                    !timeSlot.available -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    selected -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    professionalId: String = "1",
    serviceId: String = "1",
    onBackClick: () -> Unit = {},
    onBookingConfirmed: () -> Unit = {}
) {
    val today = LocalDate.now()
    val nextDays = (0..14).map { today.plusDays(it.toLong()) }
    
    val timeSlots = listOf(
        TimeSlot(LocalTime.of(9, 0)),
        TimeSlot(LocalTime.of(10, 0)),
        TimeSlot(LocalTime.of(11, 0)),
        TimeSlot(LocalTime.of(13, 0), false),
        TimeSlot(LocalTime.of(14, 0)),
        TimeSlot(LocalTime.of(15, 0)),
        TimeSlot(LocalTime.of(16, 0)),
        TimeSlot(LocalTime.of(17, 0))
    )
    
    var selectedDate by remember { mutableStateOf(today) }
    var selectedTimeSlot by remember { mutableStateOf<TimeSlot?>(null) }
    var notes by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }
    
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Agendamento Confirmado!") },
            text = { 
                Text(
                    "Seu agendamento foi realizado com sucesso para o dia ${selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} às ${selectedTimeSlot?.time?.format(DateTimeFormatter.ofPattern("HH:mm"))}."
                ) 
            },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onBookingConfirmed()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                title = { Text("Agendar") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Calendário
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Selecione uma data",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("pt", "BR"))),
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(nextDays) { date ->
                            DateItem(
                                date = date,
                                selected = date == selectedDate,
                                onDateSelected = { selectedDate = it }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Horários disponíveis
            Text(
                text = "Horários Disponíveis",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Grid de horários
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    timeSlots.take(4).forEach { slot ->
                        Box(modifier = Modifier.weight(1f)) {
                            TimeSlotItem(
                                timeSlot = slot,
                                selected = selectedTimeSlot == slot,
                                onTimeSelected = { selectedTimeSlot = it }
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    timeSlots.drop(4).forEach { slot ->
                        Box(modifier = Modifier.weight(1f)) {
                            TimeSlotItem(
                                timeSlot = slot,
                                selected = selectedTimeSlot == slot,
                                onTimeSelected = { selectedTimeSlot = it }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Divider()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Notas adicionais
            Text(
                text = "Notas ou Instruções Adicionais",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("Digite qualquer informação adicional que o profissional precise saber...") }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Botão de confirmar
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF113354),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                enabled = selectedTimeSlot != null
            ) {
                Text("Confirmar Agendamento", style = MaterialTheme.typography.titleMedium)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingScreenPreview() {
    BookingScreen()
} 