package br.com.agendou.domain.usecases.auth

import br.com.agendou.common.ErrorMessages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthUseCaseImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthUseCase {

    override val currentUser: FirebaseUser?
        get() = auth.currentUser

    override val isUserAuthenticated: Boolean
        get() = auth.currentUser != null

    override val authState: Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun signIn(email: String, password: String): Result<FirebaseUser> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        result.user?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Falha ao fazer login"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signUp(email: String, password: String): Result<FirebaseUser> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Falha ao criar conta"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signOut(): Result<Unit> = try {
        auth.signOut()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun resetPassword(email: String): Result<Unit> = try {
        auth.sendPasswordResetEmail(email).await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            auth.currentUser?.updatePassword(newPassword)?.await()
                ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        return try {
            auth.currentUser?.delete()?.await()
                ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 