package br.com.agendou.domain.usecases.auth

import br.com.agendou.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<FirebaseUser> {
        return authRepository.login(email, password)
    }
} 