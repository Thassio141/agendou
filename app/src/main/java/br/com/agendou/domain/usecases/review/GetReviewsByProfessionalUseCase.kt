package br.com.agendou.domain.usecases.review

import br.com.agendou.data.repository.ReviewRepository
import br.com.agendou.domain.models.Review
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewsByProfessionalUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    operator fun invoke(professionalRef: DocumentReference): Flow<List<Review>> {
        return reviewRepository.getReviewsByProfessional(professionalRef)
    }
} 