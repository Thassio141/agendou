package br.com.agendou.ui.appointments

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class AppointmentStatus {
    SCHEDULED,
    COMPLETED,
    CANCELED
}

data class Appointment(
    val id: String,
    val professionalName: String,
    val serviceName: String,
    val dateTime: LocalDateTime,
    val status: AppointmentStatus
)

@Composable
fun AppointmentStatusBadge(status: AppointmentStatus) {
    val (backgroundColor, textColor, text) = when (status) {
        AppointmentStatus.SCHEDULED -> Triple(
            Color(0xFF1976D2).copy(alpha = 0.2f),
            Color(0xFF1976D2),
            "Agendado"
        )
        AppointmentStatus.COMPLETED -> Triple(
            Color(0xFF43A047).copy(alpha = 0.2f),
            Color(0xFF43A047),
            "Concluído"
        )
        AppointmentStatus.CANCELED -> Triple(
            Color(0xFFE53935).copy(alpha = 0.2f),
            Color(0xFFE53935),
            "Cancelado"
        )
    }
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AppointmentItem(
    appointment: Appointment,
    onCancelClick: (String) -> Unit = {},
    onEvaluateClick: (String) -> Unit = {}
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column {
                        Text(
                            text = appointment.professionalName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = appointment.serviceName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                
                AppointmentStatusBadge(appointment.status)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Data: ${appointment.dateTime.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Text(
                    text = "Horário: ${appointment.dateTime.format(timeFormatter)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (appointment.status == AppointmentStatus.SCHEDULED) {
                    Button(
                        onClick = { onCancelClick(appointment.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Cancelar")
                    }
                } else if (appointment.status == AppointmentStatus.COMPLETED) {
                    Button(
                        onClick = { onEvaluateClick(appointment.id) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Avaliar")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(
    onBackClick: () -> Unit = {},
    onCancelAppointment: (String) -> Unit = {},
    onEvaluateAppointment: (String) -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    val allAppointments = listOf(
        Appointment(
            id = "1",
            professionalName = "Dra. Amanda Ramos",
            serviceName = "Limpeza Dental",
            dateTime = LocalDateTime.now().plusDays(5),
            status = AppointmentStatus.SCHEDULED
        ),
        Appointment(
            id = "2",
            professionalName = "Michael Souza",
            serviceName = "Corte de Cabelo",
            dateTime = LocalDateTime.now().plusDays(2),
            status = AppointmentStatus.SCHEDULED
        ),
        Appointment(
            id = "3",
            professionalName = "Larissa Melo",
            serviceName = "Massagem Relaxante",
            dateTime = LocalDateTime.now().minusDays(10),
            status = AppointmentStatus.COMPLETED
        ),
        Appointment(
            id = "4",
            professionalName = "Alexandre Pereira",
            serviceName = "Treino Personalizado",
            dateTime = LocalDateTime.now().minusDays(15),
            status = AppointmentStatus.CANCELED
        )
    )
    
    val filteredAppointments = when (selectedTabIndex) {
        0 -> allAppointments.filter { it.status == AppointmentStatus.SCHEDULED }
        1 -> allAppointments.filter { it.status == AppointmentStatus.COMPLETED }
        else -> allAppointments.filter { it.status == AppointmentStatus.CANCELED }
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
                title = { Text("Meus Agendamentos") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Agendados") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Concluídos") }
                )
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = { Text("Cancelados") }
                )
            }
            
            if (filteredAppointments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum agendamento ${
                            when (selectedTabIndex) {
                                0 -> "pendente"
                                1 -> "concluído"
                                else -> "cancelado"
                            }
                        } encontrado.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
                ) {
                    items(filteredAppointments) { appointment ->
                        AppointmentItem(
                            appointment = appointment,
                            onCancelClick = onCancelAppointment,
                            onEvaluateClick = onEvaluateAppointment
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyAppointmentsScreenPreview() {
    MyAppointmentsScreen()
} 