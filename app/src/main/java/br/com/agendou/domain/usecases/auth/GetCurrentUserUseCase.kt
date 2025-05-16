package br.com.agendou.domain.usecases.auth

import br.com.agendou.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): FirebaseUser? {
        return authRepository.currentUser
    }
    
    fun getUserFlow(): Flow<FirebaseUser?> {
        return authRepository.currentUserFlow
    }
} 