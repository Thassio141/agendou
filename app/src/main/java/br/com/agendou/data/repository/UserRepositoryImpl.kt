package br.com.agendou.data.repository

import br.com.agendou.data.util.toFlow
import br.com.agendou.domain.models.User
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    firestore: FirebaseFirestore
) : UserRepository {

    private val col = firestore.collection("users")

    override suspend fun createUser(user: User): Result<User> = runCatching {
        val docRef = col.document()
        val now    = Timestamp.now()

        val toSave = user.copy(
            createdAt = now,
            updatedAt = now
        )

        docRef.set(toSave).await()
        toSave.copy(id = docRef.id)
    }

    override suspend fun getUserById(id: String): Result<User> = runCatching {
        val snap = col.document(id).get().await()
        snap.toObject(User::class.java)
            ?.apply { this.id = snap.id }
            ?: error("User not found")
    }

    override suspend fun updateUser(user: User): Result<User> = runCatching {
        require(user.id.isNotBlank()) { "Cannot update User without id" }
        val now    = Timestamp.now()
        val toSave = user.copy(updatedAt = now)

        col.document(user.id)
            .set(toSave, SetOptions.merge())
            .await()

        toSave
    }

    override suspend fun deleteUser(id: String): Result<Unit> = runCatching {
        col.document(id).delete().await()
    }

    override fun getAllUsers(): Flow<List<User>> =
        col.orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()

    override fun getUsersByCategory(categoryRef: DocumentReference): Flow<List<User>> =
        col.whereEqualTo("categoryRef", categoryRef)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()
}
