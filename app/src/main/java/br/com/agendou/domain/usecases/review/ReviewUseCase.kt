package br.com.agendou.domain.usecases.review

import br.com.agendou.domain.models.Review
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ReviewUseCase {
    suspend fun createReview(review: Review): Result<Review>
    suspend fun getReviewById(id: String): Result<Review>
    suspend fun updateReview(review: Review): Result<Review>
    suspend fun deleteReview(id: String): Result<Unit>
    fun getAllReviews(): Flow<List<Review>>
    fun getReviewsByClient(clientRef: DocumentReference): Flow<List<Review>>
    fun getReviewsByProfessional(professionalRef: DocumentReference): Flow<List<Review>>
    fun getReviewsByAppointment(appointmentRef: DocumentReference): Flow<List<Review>>
    suspend fun getClientReviews(): Result<List<Review>>
    suspend fun getProfessionalReviews(): Result<List<Review>>
} 