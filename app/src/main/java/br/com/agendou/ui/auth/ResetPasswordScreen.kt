package br.com.agendou.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.agendou.R

@Composable
fun ResetPasswordScreen(
    onResetPasswordClick: (newPassword: String) -> Unit = { _ -> },
    onBackClick: () -> Unit = {}
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var passwordsMatch by remember { mutableStateOf(true) }

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
            "Redefinir Senha",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            "Crie uma nova senha segura para sua conta.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = newPassword,
            onValueChange = { 
                newPassword = it
                passwordsMatch = newPassword == confirmPassword || confirmPassword.isEmpty()
            },
            label = { Text("Nova senha") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        )
        
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { 
                confirmPassword = it
                passwordsMatch = newPassword == confirmPassword || confirmPassword.isEmpty()
            },
            label = { Text("Confirmar nova senha") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = !passwordsMatch,
            supportingText = {
                if (!passwordsMatch) {
                    Text("As senhas não correspondem", color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        )
        
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { onResetPasswordClick(newPassword) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF113354),
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = newPassword.isNotEmpty() && confirmPassword.isNotEmpty() && passwordsMatch
        ) {
            Text("Confirmar Nova Senha", style = MaterialTheme.typography.labelLarge)
        }
        
        Spacer(Modifier.height(16.dp))
        
        TextButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordScreenPreview() {
    ResetPasswordScreen()
} 