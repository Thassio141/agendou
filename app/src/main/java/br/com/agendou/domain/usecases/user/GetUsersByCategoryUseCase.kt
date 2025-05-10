package br.com.agendou.domain.usecases.user

import br.com.agendou.data.repository.UserRepository
import br.com.agendou.domain.models.User
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersByCategoryUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(categoryRef: DocumentReference): Flow<List<User>> {
        return userRepository.getUsersByCategory(categoryRef)
    }
} 