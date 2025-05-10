package br.com.agendou.data.repository

import br.com.agendou.domain.models.WorkSchedule
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await

class WorkScheduleRepositoryImpl(
    firestore: FirebaseFirestore
) : WorkScheduleRepository {

    private val collection = firestore.collection("workSchedules")

    override suspend fun createWorkSchedule(workSchedule: WorkSchedule): Result<WorkSchedule> = try {
        val now = Timestamp.now()
        val docRef = if (workSchedule.id.isNotBlank()) collection.document(workSchedule.id) else collection.document()
        val wsWithMeta = workSchedule.copy(
            id = docRef.id,
            createdAt = now,
            updatedAt = now
        )
        docRef.set(wsWithMeta).await()
        Result.success(wsWithMeta)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getWorkScheduleById(id: String): Result<WorkSchedule> = try {
        val doc = collection.document(id).get().await()
        val ws = doc.toObject(WorkSchedule::class.java)?.apply { this.id = doc.id }
        if (ws != null) Result.success(ws) else Result.failure(Exception("WorkSchedule not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateWorkSchedule(workSchedule: WorkSchedule): Result<WorkSchedule> = try {
        val now = Timestamp.now()
        val wsWithMeta = workSchedule.copy(updatedAt = now)
        collection.document(workSchedule.id).set(wsWithMeta, SetOptions.merge()).await()
        Result.success(wsWithMeta)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteWorkSchedule(id: String): Result<Unit> = try {
        collection.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getAllWorkSchedules(): Flow<List<WorkSchedule>> = callbackFlow {
        val sub = collection.addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            val list = snap?.documents?.mapNotNull { doc ->
                doc.toObject(WorkSchedule::class.java)?.apply { this.id = doc.id }
            } ?: emptyList()
            trySend(list)
        }
        awaitClose { sub.remove() }
    }

    override fun getWorkSchedulesByProfessional(professionalRef: DocumentReference): Flow<List<WorkSchedule>> = callbackFlow {
        val sub = collection.whereEqualTo("professionalRef", professionalRef)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                val list = snap?.documents?.mapNotNull { doc ->
                    doc.toObject(WorkSchedule::class.java)?.apply { this.id = doc.id }
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { sub.remove() }
    }
}
