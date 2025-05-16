package br.com.agendou.domain.usecases.auth

import br.com.agendou.data.repository.AuthRepository
import javax.inject.Inject

class IsUserAuthenticatedUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Boolean {
        return authRepository.isUserAuthenticated()
    }
} 