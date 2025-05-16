package br.com.agendou.data.repository

import br.com.agendou.data.util.toFlow
import br.com.agendou.domain.models.WorkSchedule
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow

class WorkScheduleRepositoryImpl(
    firestore: FirebaseFirestore
) : WorkScheduleRepository {

    private val col = firestore.collection("workSchedules")

    override suspend fun createWorkSchedule(ws: WorkSchedule): Result<WorkSchedule> = runCatching {
        val docRef = col.document()
        val now    = Timestamp.now()

        val toSave = ws.copy(
            createdAt = now,
            updatedAt = now
        )

        docRef.set(toSave).await()
        toSave.copy(id = docRef.id)
    }

    override suspend fun getWorkScheduleById(id: String): Result<WorkSchedule> = runCatching {
        val snap = col.document(id).get().await()
        snap.toObject(WorkSchedule::class.java)
            ?.apply { this.id = snap.id }
            ?: error("WorkSchedule not found")
    }

    override suspend fun updateWorkSchedule(ws: WorkSchedule): Result<WorkSchedule> = runCatching {
        require(ws.id.isNotBlank()) { "Cannot update WorkSchedule without an id" }
        val now    = Timestamp.now()
        val toSave = ws.copy(updatedAt = now)

        col.document(ws.id)
            .set(toSave, SetOptions.merge())
            .await()

        toSave
    }

    override suspend fun deleteWorkSchedule(id: String): Result<Unit> = runCatching {
        col.document(id).delete().await()
    }

    override fun getAllWorkSchedules(): Flow<List<WorkSchedule>> =
        col.orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()

    override fun getWorkSchedulesByProfessional(professionalRef: DocumentReference): Flow<List<WorkSchedule>> =
        col.whereEqualTo("professionalRef", professionalRef)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()
}
