package br.com.agendou.domain.usecases.review

import br.com.agendou.data.repository.ReviewRepository
import br.com.agendou.domain.models.Review
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewsByClientUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    operator fun invoke(clientRef: DocumentReference): Flow<List<Review>> {
        return reviewRepository.getReviewsByClient(clientRef)
    }
} 