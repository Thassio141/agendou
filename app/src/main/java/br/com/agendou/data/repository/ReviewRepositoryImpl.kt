package br.com.agendou.data.repository

import br.com.agendou.domain.models.Review
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ReviewRepositoryImpl(
    firestore: FirebaseFirestore
) : ReviewRepository {

    private val reviewsCollection = firestore.collection("reviews")

    override suspend fun createReview(review: Review): Result<Review> = try {
        val reviewWithTimestamps = review.copy(
            createdAt = Timestamp.now(),
        )
        val docRef = reviewsCollection.document(review.id)
        docRef.set(reviewWithTimestamps).await()
        Result.success(reviewWithTimestamps)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getReviewById(id: String): Result<Review> = try {
        val doc = reviewsCollection.document(id).get().await()
        val review = doc.toObject(Review::class.java)
        if (review != null) {
            Result.success(review)
        } else {
            Result.failure(Exception("Review not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateReview(review: Review): Result<Review> = try {
        val reviewWithTimestamp = review.copy(createdAt = review.createdAt)
        reviewsCollection.document(review.id).set(reviewWithTimestamp).await()
        Result.success(reviewWithTimestamp)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteReview(id: String): Result<Unit> = try {
        reviewsCollection.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getAllReviews(): Flow<List<Review>> = callbackFlow {
        val subscription = reviewsCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val reviews = snapshot?.documents?.mapNotNull { it.toObject(Review::class.java) } ?: emptyList()
                trySend(reviews)
            }
        awaitClose { subscription.remove() }
    }

    override fun getReviewsByClient(clientRef: DocumentReference): Flow<List<Review>> = callbackFlow {
        val subscription = reviewsCollection
            .whereEqualTo("clientRef", clientRef)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val reviews = snapshot?.documents?.mapNotNull { it.toObject(Review::class.java) } ?: emptyList()
                trySend(reviews)
            }
        awaitClose { subscription.remove() }
    }

    override fun getReviewsByProfessional(professionalRef: DocumentReference): Flow<List<Review>> = callbackFlow {
        val subscription = reviewsCollection
            .whereEqualTo("professionalRef", professionalRef)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val reviews = snapshot?.documents?.mapNotNull { it.toObject(Review::class.java) } ?: emptyList()
                trySend(reviews)
            }
        awaitClose { subscription.remove() }
    }

    override fun getReviewsByAppointment(appointmentRef: DocumentReference): Flow<List<Review>> = callbackFlow {
        val subscription = reviewsCollection
            .whereEqualTo("appointmentRef", appointmentRef)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val reviews = snapshot?.documents?.mapNotNull { it.toObject(Review::class.java) } ?: emptyList()
                trySend(reviews)
            }
        awaitClose { subscription.remove() }
    }
}