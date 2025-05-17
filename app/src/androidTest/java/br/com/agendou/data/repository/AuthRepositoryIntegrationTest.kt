package br.com.agendou.data.repository

import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.agendou.domain.enums.UserType
import br.com.agendou.domain.models.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
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
class AuthRepositoryIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authRepository: AuthRepository

    private val testEmail = "teste_integracao@example.com"
    private val testPassword = "Senha@123"

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @After
    fun tearDown() = runTest {
        if (authRepository.isUserAuthenticated()) {
            authRepository.logout()
        }
    }

    @Test
    fun testLoginAndLogout() = runTest {
        // Primeiro registra um usuário para garantir que existe
        val user = User(
            name = "Usuário de Integração",
            email = testEmail,
            type = UserType.CLIENT
        )
        
        // Tenta registrar (pode falhar se o usuário já existir, o que é aceitável para teste)
        try {
            authRepository.register(testEmail, testPassword, user)
        } catch (e: Exception) {
            // Ignora se o usuário já existir
        }
        
        // Testa login
        val loginResult = authRepository.login(testEmail, testPassword)
        assertTrue(loginResult.isSuccess)
        
        // Verifica se está autenticado
        assertTrue(authRepository.isUserAuthenticated())
        
        // Testa logout
        val logoutResult = authRepository.logout()
        assertTrue(logoutResult.isSuccess)
        
        // Verifica se não está mais autenticado
        assertEquals(false, authRepository.isUserAuthenticated())
    }

    @Test
    fun testResetPassword() = runTest {
        val resetResult = authRepository.resetPassword(testEmail)
        assertTrue(resetResult.isSuccess)
    }
} 