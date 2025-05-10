package br.com.agendou.data.repository

import br.com.agendou.domain.models.Service
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await

class ServiceRepositoryImpl(
    firestore: FirebaseFirestore
) : ServiceRepository {

    private val collection = firestore.collection("services")

    override suspend fun createService(service: Service): Result<Service> = try {
        val now = Timestamp.now()
        val docRef = if (service.id.isNotBlank()) collection.document(service.id) else collection.document()
        val svcWithMeta = service.copy(
            id = docRef.id,
            createdAt = now,
            updatedAt = now
        )
        docRef.set(svcWithMeta).await()
        Result.success(svcWithMeta)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getServiceById(id: String): Result<Service> = try {
        val doc = collection.document(id).get().await()
        val svc = doc.toObject(Service::class.java)?.apply { this.id = doc.id }
        if (svc != null) Result.success(svc) else Result.failure(Exception("Service not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateService(service: Service): Result<Service> = try {
        val now = Timestamp.now()
        val svcWithMeta = service.copy(updatedAt = now)
        collection.document(service.id).set(svcWithMeta, SetOptions.merge()).await()
        Result.success(svcWithMeta)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteService(id: String): Result<Unit> = try {
        collection.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getAllServices(): Flow<List<Service>> = callbackFlow {
        val sub = collection.addSnapshotListener { snap, err ->
            if (err != null) { close(err); return@addSnapshotListener }
            val list = snap?.documents?.mapNotNull { doc ->
                doc.toObject(Service::class.java)?.apply { this.id = doc.id }
            } ?: emptyList()
            trySend(list)
        }
        awaitClose { sub.remove() }
    }

    override fun getServicesByProfessional(professionalRef: DocumentReference): Flow<List<Service>> = callbackFlow {
        val sub = collection.whereEqualTo("professionalRef", professionalRef)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                val list = snap?.documents?.mapNotNull { doc ->
                    doc.toObject(Service::class.java)?.apply { this.id = doc.id }
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { sub.remove() }
    }

    override fun getServicesByCategory(categoryRef: DocumentReference): Flow<List<Service>> = callbackFlow {
        val sub = collection.whereEqualTo("categoryRef", categoryRef)
            .addSnapshotListener { snap, err ->
                if (err != null) { close(err); return@addSnapshotListener }
                val list = snap?.documents?.mapNotNull { doc ->
                    doc.toObject(Service::class.java)?.apply { this.id = doc.id }
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { sub.remove() }
    }
}
