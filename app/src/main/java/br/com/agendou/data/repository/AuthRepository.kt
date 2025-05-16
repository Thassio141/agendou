package br.com.agendou.data.repository

import br.com.agendou.domain.models.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    
    val currentUserFlow: Flow<FirebaseUser?>
    
    suspend fun login(email: String, password: String): Result<FirebaseUser>
    
    suspend fun register(email: String, password: String, user: User): Result<FirebaseUser>
    
    suspend fun loginWithGoogle(credential: AuthCredential): Result<FirebaseUser>
    
    suspend fun loginAnonymously(): Result<FirebaseUser>
    
    suspend fun logout(): Result<Unit>
    
    suspend fun resetPassword(email: String): Result<Unit>
    
    suspend fun updateUserProfile(name: String, photoUri: String? = null): Result<Unit>
    
    fun isUserAuthenticated(): Boolean
} 