package br.com.agendou.data.repository

import br.com.agendou.data.util.toFlow
import br.com.agendou.domain.models.Review
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ReviewRepositoryImpl(
    firestore: FirebaseFirestore
) : ReviewRepository {

    private val col = firestore.collection("reviews")

    override suspend fun createReview(review: Review): Result<Review> = runCatching {
        val now    = Timestamp.now()
        val docRef = col.document()
        val toSave = review.copy(createdAt = now)
        docRef.set(toSave).await()
        toSave.copy(id = docRef.id)
    }

    override suspend fun getReviewById(id: String): Result<Review> = runCatching {
        val snap = col.document(id).get().await()
        snap.toObject(Review::class.java)
            ?.apply { this.id = snap.id }
            ?: throw Exception("Review not found")
    }

    override suspend fun updateReview(review: Review): Result<Review> = runCatching {
        require(review.id.isNotBlank())
        val toSave = review.copy(createdAt= review.createdAt)
        col.document(review.id).set(toSave, SetOptions.merge()).await()
        toSave
    }

    override suspend fun deleteReview(id: String): Result<Unit> = runCatching {
        col.document(id).delete().await()
    }

    override fun getAllReviews(): Flow<List<Review>> =
        col.orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()

    override fun getReviewsByClient(clientRef: DocumentReference): Flow<List<Review>> =
        col.whereEqualTo("clientRef", clientRef)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()

    override fun getReviewsByProfessional(proRef: DocumentReference): Flow<List<Review>> =
        col.whereEqualTo("professionalRef", proRef)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()

    override fun getReviewsByAppointment(appointmentRef: DocumentReference): Flow<List<Review>> =
        col.whereEqualTo("appointmentRef", appointmentRef)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()
}
