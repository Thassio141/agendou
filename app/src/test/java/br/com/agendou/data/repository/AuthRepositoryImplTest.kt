package br.com.agendou.data.repository

import br.com.agendou.domain.enums.UserType
import br.com.agendou.domain.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var firestore: FirebaseFirestore

    @Mock
    private lateinit var usersCollection: CollectionReference

    @Mock
    private lateinit var userDocRef: DocumentReference

    @Mock
    private lateinit var authTask: Task<AuthResult>

    @Mock
    private lateinit var authResult: AuthResult

    @Mock
    private lateinit var firebaseUser: FirebaseUser

    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        `when`(firestore.collection("users")).thenReturn(usersCollection)
        `when`(usersCollection.document(Mockito.anyString())).thenReturn(userDocRef)
        
        repository = AuthRepositoryImpl(firebaseAuth, firestore)
    }

    @Test
    fun `login com credenciais válidas retorna sucesso`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"

        `when`(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(authTask)
        `when`(authTask.isSuccessful).thenReturn(true)
        `when`(authTask.result).thenReturn(authResult)
        `when`(authResult.user).thenReturn(firebaseUser)

        // Act
        val result = repository.login(email, password)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(firebaseUser, result.getOrNull())
    }

    @Test
    fun `registro com dados válidos retorna sucesso`() = runTest {
        // Arrange
        val email = "novo@example.com"
        val password = "senha123"
        val user = User(name = "Novo Usuário", email = email, type = UserType.CLIENT)

        `when`(firebaseAuth.createUserWithEmailAndPassword(email, password)).thenReturn(authTask)
        `when`(authTask.isSuccessful).thenReturn(true)
        `when`(authTask.result).thenReturn(authResult)
        `when`(authResult.user).thenReturn(firebaseUser)
        `when`(firebaseUser.uid).thenReturn("user123")
        
        `when`(userDocRef.set(Mockito.any())).thenReturn(Task.forResult(null))

        // Act
        val result = repository.register(email, password, user)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(firebaseUser, result.getOrNull())
    }

    @Test
    fun `logout retorna sucesso quando bem-sucedido`() = runTest {
        // Act
        val result = repository.logout()

        // Assert
        assertTrue(result.isSuccess)
    }
} 