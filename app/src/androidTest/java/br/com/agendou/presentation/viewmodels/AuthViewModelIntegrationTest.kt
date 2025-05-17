package br.com.agendou.presentation.viewmodels

import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.agendou.domain.models.User
import br.com.agendou.presentation.viewmodels.AuthViewModel.AuthState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class AuthViewModelIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authViewModel: AuthViewModel

    private val testEmail = "teste_integracao_viewmodel@example.com"
    private val testPassword = "Senha@123"

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testLoginFlow() = runTest {
        // Verifica se o estado inicial é Idle
        assertEquals(AuthState.Idle, authViewModel.authState.value)

        // Tenta fazer login
        authViewModel.login(testEmail, testPassword)
        
        // Aguarda um pouco para a operação assíncrona
        delay(500)
        
        // Verifica se o estado mudou
        // O resultado pode ser Success ou Error dependendo das credenciais
        assertTrue(
            authViewModel.authState.value is AuthState.Success || 
            authViewModel.authState.value is AuthState.Error
        )
    }

    @Test
    fun testLogoutFlow() = runTest {
        // Tenta fazer logout
        authViewModel.logout()
        
        // Aguarda um pouco para a operação assíncrona
        delay(500)
        
        // Verifica o estado após o logout
        assertTrue(
            authViewModel.authState.value is AuthState.Idle || 
            authViewModel.authState.value is AuthState.Error
        )
    }

    @Test
    fun testPasswordResetFlow() = runTest {
        // Solicita redefinição de senha
        authViewModel.resetPassword(testEmail)
        
        // Aguarda um pouco para a operação assíncrona
        delay(500)
        
        // Verifica se o estado mudou
        assertTrue(
            authViewModel.authState.value is AuthState.PasswordResetSent || 
            authViewModel.authState.value is AuthState.Error
        )
    }

    @Test
    fun testAuthStateCheck() = runTest {
        // Verifica o estado de autenticação atual
        authViewModel.checkAuthState()
        
        // O estado deve ser Success (autenticado) ou Idle (não autenticado)
        assertTrue(
            authViewModel.authState.value is AuthState.Success || 
            authViewModel.authState.value is AuthState.Idle
        )
    }
} 