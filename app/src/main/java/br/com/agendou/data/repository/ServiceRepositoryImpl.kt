package br.com.agendou.data.repository

import br.com.agendou.data.util.toFlow
import br.com.agendou.domain.models.Service
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class ServiceRepositoryImpl(
    firestore: FirebaseFirestore
) : ServiceRepository {

    private val col = firestore.collection("services")

    override suspend fun createService(service: Service): Result<Service> = runCatching {
        val now = Timestamp.now()
        val docRef = col.document()
        val toSave = service.copy(createdAt = now, updatedAt = now)
        docRef.set(toSave).await()
        toSave.copy(id = docRef.id)
    }

    override suspend fun getServiceById(id: String): Result<Service> = runCatching {
        val snap = col.document(id).get().await()
        snap.toObject(Service::class.java)?.apply { this.id = snap.id }
            ?: throw Exception("Service not found")
    }

    override suspend fun updateService(service: Service): Result<Service> = runCatching {
        require(service.id.isNotBlank())
        val now = Timestamp.now()
        val toSave = service.copy(updatedAt = now)
        col.document(service.id).set(toSave, SetOptions.merge()).await()
        toSave
    }

    override suspend fun deleteService(id: String): Result<Unit> = runCatching {
        col.document(id).delete().await()
    }

    override fun getAllServices(): Flow<List<Service>> =
        col.orderBy("createdAt", Query.Direction.DESCENDING).toFlow()

    override fun getServicesByProfessional(professionalRef: DocumentReference): Flow<List<Service>> =
        col.whereEqualTo("professionalRef", professionalRef)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()

    override fun getServicesByCategory(categoryRef: DocumentReference): Flow<List<Service>> =
        col.whereEqualTo("categoryRef", categoryRef)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()
}
