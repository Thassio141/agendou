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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.agendou.R

@Composable
fun VerificationCodeScreen(
    email: String,
    onVerifyCodeClick: (code: String) -> Unit = { _ -> },
    onBackClick: () -> Unit = {}
) {
    var verificationCode by remember { mutableStateOf("") }

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
            "Verificar Código",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            "Enviamos um código de verificação de 6 dígitos para $email. Por favor, insira o código abaixo.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = verificationCode,
            onValueChange = { 
                // Limita a entrada a 6 caracteres
                if (it.length <= 6) verificationCode = it 
            },
            label = { Text("Código de 6 dígitos") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                keyboardType = KeyboardType.Text
            )
        )
        
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { onVerifyCodeClick(verificationCode) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF113354),
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = verificationCode.length == 6
        ) {
            Text("Verificar", style = MaterialTheme.typography.labelLarge)
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
fun VerificationCodeScreenPreview() {
    VerificationCodeScreen(email = "usuario@exemplo.com")
} 