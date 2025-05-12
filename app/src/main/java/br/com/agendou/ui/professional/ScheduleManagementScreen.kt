package br.com.agendou.ui.professional

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import java.util.UUID

data class WorkingHours(
    val dayOfWeek: DayOfWeek,
    val isWorkingDay: Boolean = true,
    val startTime: LocalTime = LocalTime.of(9, 0),
    val endTime: LocalTime = LocalTime.of(18, 0)
)

data class DayOff(
    val id: String = UUID.randomUUID().toString(),
    val date: LocalDate,
    val description: String = ""
)

enum class ScheduleTab {
    WEEKDAYS, EXCEPTIONS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleManagementScreen(
    initialWorkingHours: List<WorkingHours> = DayOfWeek.values().map { 
        WorkingHours(
            dayOfWeek = it,
            isWorkingDay = it != DayOfWeek.SUNDAY && it != DayOfWeek.SATURDAY,
            startTime = LocalTime.of(9, 0),
            endTime = LocalTime.of(18, 0)
        )
    },
    initialDaysOff: List<DayOff> = listOf(
        DayOff(date = LocalDate.now().plusDays(5), description = "Feriado"),
        DayOff(date = LocalDate.now().plusDays(15), description = "Férias")
    ),
    onBackClick: () -> Unit = {}
) {
    val workingHours = remember { mutableStateListOf<WorkingHours>().apply { 
        addAll(initialWorkingHours) 
    } }
    
    val daysOff = remember { mutableStateListOf<DayOff>().apply { 
        addAll(initialDaysOff) 
    } }
    
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddDayOffDialog by remember { mutableStateOf(false) }
    var showEditHoursDialog by remember { mutableStateOf(false) }
    var currentDayOfWeek by remember { mutableStateOf<DayOfWeek?>(null) }
    var showDeleteDayOffConfirmation by remember { mutableStateOf(false) }
    var dayOffToDelete by remember { mutableStateOf<DayOff?>(null) }
    
    if (showEditHoursDialog && currentDayOfWeek != null) {
        val index = workingHours.indexOfFirst { it.dayOfWeek == currentDayOfWeek }
        if (index >= 0) {
            val currentHours = workingHours[index]
            var isWorkingDay by remember { mutableStateOf(currentHours.isWorkingDay) }
            var startHour by remember { mutableIntStateOf(currentHours.startTime.hour) }
            var startMinute by remember { mutableIntStateOf(currentHours.startTime.minute) }
            var endHour by remember { mutableIntStateOf(currentHours.endTime.hour) }
            var endMinute by remember { mutableIntStateOf(currentHours.endTime.minute) }
            var showStartTimePicker by remember { mutableStateOf(false) }
            var showEndTimePicker by remember { mutableStateOf(false) }
            
            var startPickerState = rememberTimePickerState(
                initialHour = startHour,
                initialMinute = startMinute,
                is24Hour = true
            )
            
            var endPickerState = rememberTimePickerState(
                initialHour = endHour,
                initialMinute = endMinute,
                is24Hour = true
            )
            
            if (showStartTimePicker) {
                Dialog(onDismissRequest = { showStartTimePicker = false }) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Selecione o horário de início",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TimePicker(
                                state = startPickerState,
                                colors = TimePickerDefaults.colors(
                                    timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                                    timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { showStartTimePicker = false }) {
                                    Text("Cancelar")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        startHour = startPickerState.hour
                                        startMinute = startPickerState.minute
                                        showStartTimePicker = false
                                    }
                                ) {
                                    Text("OK")
                                }
                            }
                        }
                    }
                }
            }
            
            if (showEndTimePicker) {
                Dialog(onDismissRequest = { showEndTimePicker = false }) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Selecione o horário de término",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TimePicker(
                                state = endPickerState,
                                colors = TimePickerDefaults.colors(
                                    timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                                    timeSelectorSelectedContentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = { showEndTimePicker = false }) {
                                    Text("Cancelar")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        endHour = endPickerState.hour
                                        endMinute = endPickerState.minute
                                        showEndTimePicker = false
                                    }
                                ) {
                                    Text("OK")
                                }
                            }
                        }
                    }
                }
            }
            
            AlertDialog(
                onDismissRequest = { 
                    showEditHoursDialog = false
                    currentDayOfWeek = null
                },
                title = {
                    Text(
                        "Horário de ${currentDayOfWeek?.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                text = {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isWorkingDay,
                                onCheckedChange = { isWorkingDay = it }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Dia de trabalho")
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        if (isWorkingDay) {
                            Text(
                                text = "Horário de Início",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showStartTimePicker = true },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = String.format("%02d:%02d", startHour, startMinute),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "Horário de Término",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showEndTimePicker = true },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = String.format("%02d:%02d", endHour, endMinute),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            
                            // Validação
                            val startTime = LocalTime.of(startHour, startMinute)
                            val endTime = LocalTime.of(endHour, endMinute)
                            
                            if (endTime.isBefore(startTime) || endTime == startTime) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "O horário de término deve ser depois do horário de início.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val startTime = LocalTime.of(startHour, startMinute)
                            val endTime = LocalTime.of(endHour, endMinute)
                            
                            if (!isWorkingDay || endTime.isAfter(startTime)) {
                                workingHours[index] = WorkingHours(
                                    dayOfWeek = currentDayOfWeek!!,
                                    isWorkingDay = isWorkingDay,
                                    startTime = startTime,
                                    endTime = endTime
                                )
                                showEditHoursDialog = false
                                currentDayOfWeek = null
                            }
                        },
                        enabled = !isWorkingDay || LocalTime.of(endHour, endMinute).isAfter(LocalTime.of(startHour, startMinute))
                    ) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showEditHoursDialog = false
                            currentDayOfWeek = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
    
    if (showAddDayOffDialog) {
        var dayOffDescription by remember { mutableStateOf("") }
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        )
        
        DatePickerDialog(
            onDismissRequest = { showAddDayOffDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { timeMillis ->
                            val date = Instant.ofEpochMilli(timeMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            val dayOff = DayOff(
                                date = date,
                                description = dayOffDescription
                            )
                            daysOff.add(dayOff)
                        }
                        showAddDayOffDialog = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDayOffDialog = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Selecione a data para folga ou exceção",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                DatePicker(state = datePickerState)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Descrição da Folga (opcional)",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                androidx.compose.material3.OutlinedTextField(
                    value = dayOffDescription,
                    onValueChange = { dayOffDescription = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ex: Feriado, Férias, etc") }
                )
            }
        }
    }
    
    if (showDeleteDayOffConfirmation && dayOffToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteDayOffConfirmation = false
                dayOffToDelete = null
            },
            title = { Text("Confirmar exclusão") },
            text = { 
                Text(
                    "Tem certeza que deseja excluir a folga do dia ${
                        dayOffToDelete?.date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    }?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        daysOff.removeIf { it.id == dayOffToDelete?.id }
                        showDeleteDayOffConfirmation = false
                        dayOffToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDayOffConfirmation = false
                        dayOffToDelete = null
                    }
                ) {
                    Text("Cancelar")
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
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                title = { Text("Gerenciar Agenda") }
            )
        },
        floatingActionButton = {
            if (selectedTab == 1) {
                ExtendedFloatingActionButton(
                    onClick = { showAddDayOffDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    text = {
                        Text(
                            "Adicionar Folga",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
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
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Dias da Semana") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Folgas/Exceções") }
                )
            }
            
            when (selectedTab) {
                0 -> {
                    // Horários de trabalho por dia da semana
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(workingHours.sortedBy { it.dayOfWeek.value }) { hours ->
                            WeekdayScheduleItem(
                                hours = hours,
                                onClick = {
                                    currentDayOfWeek = hours.dayOfWeek
                                    showEditHoursDialog = true
                                }
                            )
                        }
                    }
                }
                1 -> {
                    // Dias de folga/exceção
                    if (daysOff.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Nenhuma folga cadastrada",
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Adicione dias de folga para feriados, férias ou outras exceções",
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(daysOff.sortedBy { it.date }, key = { it.id }) { dayOff ->
                                DayOffItem(
                                    dayOff = dayOff,
                                    onDeleteClick = {
                                        dayOffToDelete = dayOff
                                        showDeleteDayOffConfirmation = true
                                    }
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
fun WeekdayScheduleItem(
    hours: WorkingHours,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = hours.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("pt", "BR")),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            if (hours.isWorkingDay) {
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                Text(
                    text = "${hours.startTime.format(timeFormatter)} - ${hours.endTime.format(timeFormatter)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = "Folga",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun DayOffItem(
    dayOff: DayOff,
    onDeleteClick: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", Locale("pt", "BR"))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dayOff.date.format(dateFormatter),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (dayOff.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dayOff.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Remover",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleManagementScreenPreview() {
    ScheduleManagementScreen()
} 