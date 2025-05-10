package br.com.agendou.domain.usecases.auth

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    val currentUser: FirebaseUser?
    val isUserAuthenticated: Boolean
    val authState: Flow<FirebaseUser?>
    
    suspend fun signIn(email: String, password: String): Result<FirebaseUser>
    suspend fun signUp(email: String, password: String): Result<FirebaseUser>
    suspend fun signOut(): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun updatePassword(newPassword: String): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
} 