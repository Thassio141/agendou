package br.com.agendou.domain.usecases.user

import br.com.agendou.domain.models.User
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserUseCase {
    suspend fun createUser(user: User): Result<User>
    suspend fun getUserById(id: String): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(id: String): Result<Unit>
    fun getAllUsers(): Flow<List<User>>
    fun getUsersByCategory(categoryRef: DocumentReference): Flow<List<User>>
    suspend fun getCurrentUser(): Result<User>
    suspend fun updateUserProfile(user: User): Result<User>
} 