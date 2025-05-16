package br.com.agendou.domain.usecases.user

import br.com.agendou.data.repository.UserRepository
import br.com.agendou.domain.models.User
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: String): Result<User> {
        return userRepository.getUserById(id)
    }
} 