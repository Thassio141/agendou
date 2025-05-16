package br.com.agendou.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.agendou.R

enum class UserType {
    CLIENT, PROFESSIONAL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileOnboardingScreen(
    onContinueClick: (userType: UserType, category: String?) -> Unit = { _, _ -> }
) {
    var selectedUserType by remember { mutableStateOf<UserType?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    
    val professionalCategories = listOf(
        "Cabeleireiro(a)", 
        "Manicure/Pedicure", 
        "Esteticista", 
        "Nutricionista", 
        "Personal Trainer", 
        "Massagista",
        "Dentista",
        "Psicólogo(a)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.ic_agendou),
            contentDescription = "Logo Agendou",
            modifier = Modifier.size(180.dp),
            contentScale = ContentScale.Fit
        )
        
        Spacer(Modifier.height(24.dp))
        
        Text(
            "Configuração de Perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            "Escolha o tipo de usuário que você deseja ser:",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(Modifier.height(24.dp))

        // Opção de Cliente
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = selectedUserType == UserType.CLIENT,
                    onClick = { selectedUserType = UserType.CLIENT }
                )
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedUserType == UserType.CLIENT) 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedUserType == UserType.CLIENT,
                    onClick = { selectedUserType = UserType.CLIENT }
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    "Cliente",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        
        // Opção de Profissional
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = selectedUserType == UserType.PROFESSIONAL,
                    onClick = { selectedUserType = UserType.PROFESSIONAL }
                )
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selectedUserType == UserType.PROFESSIONAL) 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedUserType == UserType.PROFESSIONAL,
                    onClick = { selectedUserType = UserType.PROFESSIONAL }
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    "Profissional",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        
        Spacer(Modifier.height(24.dp))
        
        // Menu suspenso de categorias, visível somente para profissionais
        if (selectedUserType == UserType.PROFESSIONAL) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = selectedCategory,
                    onValueChange = {},
                    label = { Text("Selecione sua categoria") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    professionalCategories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        
        Spacer(Modifier.weight(1f))
        
        Button(
            onClick = { onContinueClick(
                selectedUserType ?: UserType.CLIENT, 
                if (selectedUserType == UserType.PROFESSIONAL) selectedCategory else null
            ) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF113354),
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = selectedUserType != null && 
                    (selectedUserType == UserType.CLIENT || 
                    (selectedUserType == UserType.PROFESSIONAL && selectedCategory.isNotEmpty()))
        ) {
            Text("Continuar", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileOnboardingScreenPreview() {
    ProfileOnboardingScreen()
} 