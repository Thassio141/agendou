package br.com.agendou.domain.usecases.user

import br.com.agendou.data.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return userRepository.deleteUser(id)
    }
} 