package br.com.agendou.domain.usecases.auth

import br.com.agendou.data.repository.AuthRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(name: String, photoUri: String? = null): Result<Unit> {
        return authRepository.updateUserProfile(name, photoUri)
    }
} 