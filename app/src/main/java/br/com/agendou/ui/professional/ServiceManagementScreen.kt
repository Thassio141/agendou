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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.UUID

data class ProfessionalService(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val duration: Int, // minutos
    val price: Double
)

enum class DialogMode {
    ADD, EDIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceManagementScreen(
    initialServices: List<ProfessionalService> = listOf(
        ProfessionalService(
            name = "Limpeza Dental",
            description = "Limpeza completa com remoção de tártaro e polimento",
            duration = 60,
            price = 150.00
        ),
        ProfessionalService(
            name = "Restauração",
            description = "Restauração com resina fotopolimerizável",
            duration = 45,
            price = 200.00
        ),
        ProfessionalService(
            name = "Clareamento",
            description = "Sessão de clareamento a laser",
            duration = 90,
            price = 350.00
        )
    ),
    onBackClick: () -> Unit = {}
) {
    val services = remember { mutableStateListOf<ProfessionalService>().apply { addAll(initialServices) } }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMode by remember { mutableStateOf(DialogMode.ADD) }
    var currentServiceId by remember { mutableStateOf("") }
    var serviceName by remember { mutableStateOf("") }
    var serviceDescription by remember { mutableStateOf("") }
    var serviceDuration by remember { mutableStateOf("") }
    var servicePrice by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var serviceToDelete by remember { mutableStateOf<ProfessionalService?>(null) }
    
    // Validação
    var nameError by remember { mutableStateOf<String?>(null) }
    var durationError by remember { mutableStateOf<String?>(null) }
    var priceError by remember { mutableStateOf<String?>(null) }
    
    fun clearDialogFields() {
        serviceName = ""
        serviceDescription = ""
        serviceDuration = ""
        servicePrice = ""
        nameError = null
        durationError = null
        priceError = null
    }
    
    fun validateFields(): Boolean {
        var isValid = true
        
        if (serviceName.isBlank()) {
            nameError = "Nome é obrigatório"
            isValid = false
        } else {
            nameError = null
        }
        
        val duration = serviceDuration.toIntOrNull()
        if (duration == null || duration <= 0) {
            durationError = "Duração deve ser maior que zero"
            isValid = false
        } else {
            durationError = null
        }
        
        val price = servicePrice.toDoubleOrNull()
        if (price == null || price < 0) {
            priceError = "Preço deve ser maior ou igual a zero"
            isValid = false
        } else {
            priceError = null
        }
        
        return isValid
    }
    
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    if (dialogMode == DialogMode.ADD) "Adicionar Serviço" else "Editar Serviço",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = serviceName,
                        onValueChange = { serviceName = it },
                        label = { Text("Nome do serviço") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = nameError != null,
                        supportingText = { nameError?.let { Text(it) } }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = serviceDescription,
                        onValueChange = { serviceDescription = it },
                        label = { Text("Descrição (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = serviceDuration,
                        onValueChange = { serviceDuration = it },
                        label = { Text("Duração (minutos)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        isError = durationError != null,
                        supportingText = { durationError?.let { Text(it) } }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = servicePrice,
                        onValueChange = { servicePrice = it },
                        label = { Text("Preço (R$)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        isError = priceError != null,
                        supportingText = { priceError?.let { Text(it) } }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (validateFields()) {
                            val service = ProfessionalService(
                                id = if (dialogMode == DialogMode.ADD) UUID.randomUUID().toString() else currentServiceId,
                                name = serviceName,
                                description = serviceDescription,
                                duration = serviceDuration.toInt(),
                                price = servicePrice.toDouble()
                            )
                            
                            if (dialogMode == DialogMode.ADD) {
                                services.add(service)
                            } else {
                                val index = services.indexOfFirst { it.id == currentServiceId }
                                if (index >= 0) {
                                    services[index] = service
                                }
                            }
                            
                            showDialog = false
                            clearDialogFields()
                        }
                    }
                ) {
                    Text(if (dialogMode == DialogMode.ADD) "Adicionar" else "Salvar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        clearDialogFields()
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    if (showDeleteConfirmation && serviceToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteConfirmation = false
                serviceToDelete = null
            },
            title = { Text("Confirmar exclusão") },
            text = { Text("Tem certeza que deseja excluir o serviço '${serviceToDelete?.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        services.removeIf { it.id == serviceToDelete?.id }
                        showDeleteConfirmation = false
                        serviceToDelete = null
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
                        showDeleteConfirmation = false
                        serviceToDelete = null
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                title = { Text("Gerenciar Serviços") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    dialogMode = DialogMode.ADD
                    clearDialogFields()
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Serviço",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        if (services.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nenhum serviço cadastrado",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Clique no botão + para adicionar seus serviços",
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
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(services, key = { it.id }) { service ->
                    ServiceItem(
                        service = service,
                        onEditClick = {
                            currentServiceId = service.id
                            serviceName = service.name
                            serviceDescription = service.description
                            serviceDuration = service.duration.toString()
                            servicePrice = service.price.toString()
                            dialogMode = DialogMode.EDIT
                            showDialog = true
                        },
                        onDeleteClick = {
                            serviceToDelete = service
                            showDeleteConfirmation = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceItem(
    service: ProfessionalService,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEditClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = service.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                Text(
                    text = "R$ ${String.format("%.2f", service.price)}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            if (service.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = service.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Duração: ${service.duration} minutos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Divider()
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiceManagementScreenPreview() {
    ServiceManagementScreen()
} 