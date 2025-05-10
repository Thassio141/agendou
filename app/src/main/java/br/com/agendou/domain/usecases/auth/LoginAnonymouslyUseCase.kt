package br.com.agendou.domain.usecases.auth

import br.com.agendou.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class LoginAnonymouslyUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<FirebaseUser> {
        return authRepository.loginAnonymously()
    }
} 