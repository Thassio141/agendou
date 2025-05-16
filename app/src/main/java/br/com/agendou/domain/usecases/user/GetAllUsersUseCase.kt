package br.com.agendou.domain.usecases.user

import br.com.agendou.data.repository.UserRepository
import br.com.agendou.domain.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.getAllUsers()
    }
} 