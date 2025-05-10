package br.com.agendou.domain.usecases.review

import br.com.agendou.common.ErrorMessages
import br.com.agendou.data.repository.ReviewRepository
import br.com.agendou.domain.models.Review
import br.com.agendou.domain.usecases.auth.AuthUseCase
import br.com.agendou.domain.usecases.user.UserUseCase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewUseCaseImpl @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val authUseCase: AuthUseCase,
    private val userUseCase: UserUseCase,
    private val firestore: FirebaseFirestore
) : ReviewUseCase {

    private fun userRef(uid: String) =
        firestore.collection("users").document(uid)

    override suspend fun createReview(review: Review): Result<Review> =
        runCatching {
            val profile = userUseCase.getCurrentUser().getOrThrow()

            val toSave = review.copy(
                clientRef = userRef(profile.id)
            )
            reviewRepository.createReview(toSave).getOrThrow()
        }

    override suspend fun getReviewById(id: String): Result<Review> = 
        reviewRepository.getReviewById(id)

    override suspend fun updateReview(review: Review): Result<Review> {
        return try {
            val currentUser = authUseCase.currentUser
                ?: return Result.failure(Exception("Usuário não autenticado"))

            val existingReview = reviewRepository.getReviewById(review.id).getOrNull()
                ?: return Result.failure(Exception("Avaliação não encontrada"))

            if (existingReview.clientRef?.id != currentUser.uid) {
                return Result.failure(Exception("Não autorizado a atualizar esta avaliação"))
            }

            reviewRepository.updateReview(review)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteReview(id: String): Result<Unit> {
        return try {
            val currentUser = authUseCase.currentUser
                ?: return Result.failure(Exception("Usuário não autenticado"))

            val review = reviewRepository.getReviewById(id).getOrNull()
                ?: return Result.failure(Exception("Avaliação não encontrada"))

            if (review.clientRef?.id != currentUser.uid) {
                return Result.failure(Exception("Não autorizado a deletar esta avaliação"))
            }

            reviewRepository.deleteReview(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllReviews(): Flow<List<Review>> = 
        reviewRepository.getAllReviews()

    override fun getReviewsByClient(clientRef: DocumentReference): Flow<List<Review>> = 
        reviewRepository.getReviewsByClient(clientRef)

    override fun getReviewsByProfessional(professionalRef: DocumentReference): Flow<List<Review>> = 
        reviewRepository.getReviewsByProfessional(professionalRef)

    override fun getReviewsByAppointment(appointmentRef: DocumentReference): Flow<List<Review>> = 
        reviewRepository.getReviewsByAppointment(appointmentRef)

    override suspend fun getClientReviews(): Result<List<Review>> = try {
        val currentUser = authUseCase.currentUser
            ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))

        val clientRef = userRef(currentUser.uid)
        val reviews = reviewRepository.getReviewsByClient(clientRef).first()
        Result.success(reviews)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getProfessionalReviews(): Result<List<Review>> = try {
        val currentUser = authUseCase.currentUser
            ?: return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))

        val professionalRef = userRef(currentUser.uid)
        val reviews = reviewRepository.getReviewsByProfessional(professionalRef).first()
        Result.success(reviews)
    } catch (e: Exception) {
        Result.failure(e)
    }
} 