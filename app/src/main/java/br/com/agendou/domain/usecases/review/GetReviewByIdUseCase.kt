package br.com.agendou.domain.usecases.review

import br.com.agendou.data.repository.ReviewRepository
import br.com.agendou.domain.models.Review
import javax.inject.Inject

class GetReviewByIdUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(id: String): Result<Review> {
        return reviewRepository.getReviewById(id)
    }
} 