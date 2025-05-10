package br.com.agendou.domain.usecases.auth

import br.com.agendou.data.repository.AuthRepository
import br.com.agendou.domain.models.User
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, user: User): Result<FirebaseUser> {
        return authRepository.register(email, password, user)
    }
} 