package br.com.agendou.ui.professional

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.agendou.ui.appointments.AppointmentStatus
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class ProfessionalAppointment(
    val id: String,
    val clientName: String,
    val serviceName: String,
    val dateTime: LocalDateTime,
    val status: AppointmentStatus,
    val hasBeenReminded: Boolean = false
)

@Composable
fun DateChip(
    date: LocalDate,
    isSelected: Boolean,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    val isToday = date.isEqual(today)
    
    val dateText = when {
        isToday -> "Hoje"
        date.isEqual(today.plusDays(1)) -> "Amanhã"
        else -> date.format(DateTimeFormatter.ofPattern("dd/MM"))
    }
    
    val dayOfWeek = date.format(DateTimeFormatter.ofPattern("EEE", java.util.Locale("pt", "BR")))
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)) else null,
        modifier = Modifier
            .padding(end = 8.dp)
            .clickable { onDateSelected(date) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = dayOfWeek,
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun AppointmentCard(
    appointment: ProfessionalAppointment,
    onSendReminderClick: (ProfessionalAppointment) -> Unit = {}
) {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = appointment.clientName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Text(
                        text = appointment.serviceName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = appointment.dateTime.format(formatter),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Status ou tempo restante
                    val timeUntil = ChronoUnit.MINUTES.between(LocalDateTime.now(), appointment.dateTime)
                    when {
                        appointment.status != AppointmentStatus.SCHEDULED -> {
                            Text(
                                text = when(appointment.status) {
                                    AppointmentStatus.COMPLETED -> "Concluído"
                                    AppointmentStatus.CANCELED -> "Cancelado"
                                    else -> ""
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = if (appointment.status == AppointmentStatus.CANCELED) 
                                    MaterialTheme.colorScheme.error 
                                else 
                                    Color(0xFF43A047)
                            )
                        }
                        timeUntil <= 60 -> {
                            Text(
                                text = "Em breve",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFE65100)
                            )
                        }
                        else -> {
                            // Não exibe nada para agendamentos normais
                        }
                    }
                }
            }
            
            if (appointment.status == AppointmentStatus.SCHEDULED && !appointment.hasBeenReminded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    androidx.compose.material3.TextButton(
                        onClick = { onSendReminderClick(appointment) }
                    ) {
                        Text("Enviar lembrete")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardProScreen(
    appointments: List<ProfessionalAppointment> = generateSampleAppointments(),
    onProfileClick: () -> Unit = {},
    onServicesClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Hoje, 1: Esta semana
    
    // Gerar datas para 14 dias
    val dates = remember {
        (0..13).map { LocalDate.now().plusDays(it.toLong()) }
    }
    
    // Filtrar agendamentos
    val filteredAppointments = remember(selectedDate, selectedTab, appointments) {
        when (selectedTab) {
            0 -> appointments.filter { 
                it.dateTime.toLocalDate() == selectedDate && 
                it.status == AppointmentStatus.SCHEDULED
            }
            else -> appointments.filter { 
                val appointmentDate = it.dateTime.toLocalDate()
                val today = LocalDate.now()
                val endOfWeek = today.plusDays(6)
                appointmentDate.isAfter(today.minusDays(1)) && 
                appointmentDate.isBefore(endOfWeek.plusDays(1)) &&
                it.status == AppointmentStatus.SCHEDULED
            }
        }
    }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(Modifier.height(8.dp))
                
                Text(
                    text = "Dra. Amanda Ramos",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                
                Text(
                    text = "Dentista",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                
                Spacer(Modifier.height(24.dp))
                
                Divider()
                
                Spacer(Modifier.height(16.dp))
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Meu Perfil") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onProfileClick()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                    label = { Text("Meus Serviços") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onServicesClick()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    label = { Text("Gerenciar Agenda") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onScheduleClick()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                
                Spacer(Modifier.weight(1f))
                
                Divider()
                
                NavigationDrawerItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                    label = { Text("Sair") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        onLogoutClick()
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.error,
                        unselectedTextColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                
                Spacer(Modifier.height(24.dp))
            }
        }
    ) {
        Scaffold(
            modifier = Modifier
                .statusBarsPadding(),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    title = { Text("Dashboard Profissional") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { onScheduleClick() }) {
                            Icon(Icons.Default.Settings, contentDescription = "Configurações")
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(
                        text = { Text("Hoje") },
                        selected = selectedTab == 0,
                        onClick = { 
                            selectedTab = 0
                            selectedDate = LocalDate.now()
                        }
                    )
                    Tab(
                        text = { Text("Esta Semana") },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                }
                
                // Seletor de data (apenas visível na visualização 'Hoje')
                if (selectedTab == 0) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(dates) { date ->
                            DateChip(
                                date = date,
                                isSelected = date == selectedDate,
                                onDateSelected = { selectedDate = it }
                            )
                        }
                    }
                }
                
                // Lista de agendamentos
                if (filteredAppointments.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                modifier = Modifier.size(64.dp)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = if (selectedTab == 0) 
                                    "Nenhum agendamento para ${if (selectedDate == LocalDate.now()) "hoje" else "este dia"}" 
                                else 
                                    "Nenhum agendamento para esta semana",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            filteredAppointments.sortedBy { it.dateTime }
                        ) { appointment ->
                            AppointmentCard(
                                appointment = appointment,
                                onSendReminderClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Lembrete enviado para ${appointment.clientName}")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Função auxiliar para gerar dados de exemplo
private fun generateSampleAppointments(): List<ProfessionalAppointment> {
    val today = LocalDate.now()
    return listOf(
        ProfessionalAppointment(
            id = "1",
            clientName = "Carlos Silva",
            serviceName = "Limpeza Dental",
            dateTime = today.atTime(9, 30),
            status = AppointmentStatus.SCHEDULED
        ),
        ProfessionalAppointment(
            id = "2",
            clientName = "Maria Souza",
            serviceName = "Clareamento",
            dateTime = today.atTime(11, 0),
            status = AppointmentStatus.SCHEDULED
        ),
        ProfessionalAppointment(
            id = "3",
            clientName = "João Pereira",
            serviceName = "Restauração",
            dateTime = today.plusDays(1).atTime(14, 30),
            status = AppointmentStatus.SCHEDULED
        ),
        ProfessionalAppointment(
            id = "4",
            clientName = "Amanda Lima",
            serviceName = "Consulta de Rotina",
            dateTime = today.plusDays(2).atTime(10, 0),
            status = AppointmentStatus.SCHEDULED
        ),
        ProfessionalAppointment(
            id = "5",
            clientName = "Paulo Santos",
            serviceName = "Avaliação",
            dateTime = today.plusDays(3).atTime(16, 0),
            status = AppointmentStatus.SCHEDULED
        ),
        ProfessionalAppointment(
            id = "6",
            clientName = "Luiza Costa",
            serviceName = "Limpeza Dental",
            dateTime = today.minusDays(1).atTime(9, 0),
            status = AppointmentStatus.COMPLETED
        ),
        ProfessionalAppointment(
            id = "7",
            clientName = "Roberto Alves",
            serviceName = "Consulta de Rotina",
            dateTime = LocalDateTime.now().plusHours(1),
            status = AppointmentStatus.SCHEDULED,
            hasBeenReminded = true
        )
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardProScreenPreview() {
    DashboardProScreen()
} 