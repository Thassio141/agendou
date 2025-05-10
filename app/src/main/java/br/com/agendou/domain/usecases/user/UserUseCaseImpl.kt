package br.com.agendou.domain.usecases.user

import br.com.agendou.common.ErrorMessages
import br.com.agendou.data.repository.UserRepository
import br.com.agendou.domain.models.User
import br.com.agendou.domain.usecases.auth.AuthUseCase
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val authUseCase: AuthUseCase
) : UserUseCase {

    override suspend fun createUser(user: User): Result<User> = try {
        val currentUser = authUseCase.currentUser
            ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))
        
        val userWithAuthId = user.copy(id = currentUser.uid)
        userRepository.createUser(userWithAuthId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUserById(id: String): Result<User> = 
        userRepository.getUserById(id)

    override suspend fun updateUser(user: User): Result<User> {
        return try {
            val currentUser = authUseCase.currentUser
                ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))

            if (user.id != currentUser.uid) {
                return Result.failure(Exception("Não autorizado a atualizar este usuário"))
            }

            userRepository.updateUser(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        return try {
            val currentUser = authUseCase.currentUser
                ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))

            if (id != currentUser.uid) {
                return Result.failure(Exception("Não autorizado a deletar este usuário"))
            }

            userRepository.deleteUser(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllUsers(): Flow<List<User>> = 
        userRepository.getAllUsers()

    override fun getUsersByCategory(categoryRef: DocumentReference): Flow<List<User>> = 
        userRepository.getUsersByCategory(categoryRef)

    override suspend fun getCurrentUser(): Result<User> = try {
        val currentUser = authUseCase.currentUser
            ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))
        
        userRepository.getUserById(currentUser.uid)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateUserProfile(user: User): Result<User> {
        return try {
            val currentUser = authUseCase.currentUser
                ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))

            if (user.id != currentUser.uid) {
                return Result.failure(Exception("Não autorizado a atualizar este perfil"))
            }

            userRepository.updateUser(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 