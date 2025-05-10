package br.com.agendou.domain.usecases.user

import br.com.agendou.data.repository.UserRepository
import br.com.agendou.domain.models.User
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<User> {
        return userRepository.createUser(user)
    }
} 