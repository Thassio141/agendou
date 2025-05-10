package br.com.agendou.data.repository

import br.com.agendou.domain.models.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    firestore: FirebaseFirestore
) : UserRepository {

    private val collection = firestore.collection("users")

    override suspend fun createUser(user: User): Result<User> = try {
        val now = Timestamp.now()
        val docRef = if (user.id.isNotBlank()) collection.document(user.id) else collection.document()
        val userWithMeta = user.copy(
            id = docRef.id,
            createdAt = now,
            updatedAt = now
        )
        docRef.set(userWithMeta).await()
        Result.success(userWithMeta)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getUserById(id: String): Result<User> = try {
        val doc = collection.document(id).get().await()
        val usr = doc.toObject(User::class.java)?.apply { this.id = doc.id }
        if (usr != null) Result.success(usr) else Result.failure(Exception("User not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateUser(user: User): Result<User> = try {
        val now = Timestamp.now()
        val userWithMeta = user.copy(updatedAt = now)
        collection.document(user.id).set(userWithMeta, SetOptions.merge()).await()
        Result.success(userWithMeta)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteUser(id: String): Result<Unit> = try {
        collection.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getAllUsers(): Flow<List<User>> = callbackFlow {
        val sub = collection.addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            val list = snap?.documents?.mapNotNull { doc ->
                doc.toObject(User::class.java)?.apply { this.id = doc.id }
            } ?: emptyList()
            trySend(list)
        }
        awaitClose { sub.remove() }
    }

    override fun getUsersByCategory(categoryRef: DocumentReference): Flow<List<User>> = callbackFlow {
        val sub = collection.whereEqualTo("categoryRef", categoryRef)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                val list = snap?.documents?.mapNotNull { doc ->
                    doc.toObject(User::class.java)?.apply { this.id = doc.id }
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { sub.remove() }
    }
}
