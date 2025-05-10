package br.com.agendou.domain.usecases.review

import br.com.agendou.data.repository.ReviewRepository
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return reviewRepository.deleteReview(id)
    }
} 