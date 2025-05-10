package br.com.agendou.domain.usecases.auth

import br.com.agendou.data.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(credential: AuthCredential): Result<FirebaseUser> {
        return authRepository.loginWithGoogle(credential)
    }
} 