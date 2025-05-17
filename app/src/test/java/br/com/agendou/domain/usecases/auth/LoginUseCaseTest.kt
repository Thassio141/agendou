package br.com.agendou.domain.usecases.auth

import br.com.agendou.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class LoginUseCaseTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        loginUseCase = LoginUseCase(authRepository)
    }

    @Test
    fun `invoke com credenciais válidas retorna sucesso`() = runTest {
        // Arrange
        val email = "teste@example.com"
        val password = "senha123"

        `when`(authRepository.login(email, password)).thenReturn(Result.success(firebaseUser))

        // Act
        val result = loginUseCase(email, password)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(firebaseUser, result.getOrNull())
    }

    @Test
    fun `invoke com credenciais inválidas retorna falha`() = runTest {
        // Arrange
        val email = "invalido@example.com"
        val password = "senhaerrada"
        val errorMessage = "Credenciais inválidas"

        `when`(authRepository.login(email, password)).thenReturn(Result.failure(Exception(errorMessage)))

        // Act
        val result = loginUseCase(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke com email vazio retorna falha`() = runTest {
        // Arrange
        val email = ""
        val password = "senha123"
        val errorMessage = "E-mail não pode estar vazio"

        `when`(authRepository.login(email, password)).thenReturn(Result.failure(Exception(errorMessage)))

        // Act
        val result = loginUseCase(email, password)

        // Assert
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }
} 