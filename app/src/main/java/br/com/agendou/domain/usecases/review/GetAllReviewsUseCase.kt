package br.com.agendou.domain.usecases.review

import br.com.agendou.data.repository.ReviewRepository
import br.com.agendou.domain.models.Review
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllReviewsUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository
) {
    operator fun invoke(): Flow<List<Review>> {
        return reviewRepository.getAllReviews()
    }
} 