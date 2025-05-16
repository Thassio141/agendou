package br.com.agendou.data.repository

import br.com.agendou.data.util.toFlow
import br.com.agendou.domain.models.Appointment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class AppointmentRepositoryImpl(
    firestore: FirebaseFirestore
) : AppointmentRepository {

    private val col = firestore.collection("appointments")

    override suspend fun createAppointment(appointment: Appointment): Result<Appointment> = runCatching {
        val docRef = col.document()
        val now    = Timestamp.now()
        val toSave = appointment.copy(createdAt = now, updatedAt = now)
        docRef.set(toSave).await()
        toSave.copy(id = docRef.id)
    }

    override suspend fun getAppointmentById(id: String): Result<Appointment> = runCatching {
        val snap = col.document(id).get().await()
        snap.toObject(Appointment::class.java)
            ?.apply { this.id = snap.id }
            ?: error("Appointment not found")
    }

    override suspend fun updateAppointment(appointment: Appointment): Result<Appointment> = runCatching {
        require(appointment.id.isNotBlank())
        val now    = Timestamp.now()
        val toSave = appointment.copy(updatedAt = now)
        col.document(appointment.id)
            .set(toSave, SetOptions.merge())
            .await()
        toSave
    }

    override suspend fun deleteAppointment(id: String): Result<Unit> = runCatching {
        col.document(id).delete().await()
    }

    override fun getAllAppointments(): Flow<List<Appointment>> =
        col.orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()

    override fun getAppointmentsByClient(clientRef: DocumentReference): Flow<List<Appointment>> =
        col.whereEqualTo("clientRef", clientRef)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()

    override fun getAppointmentsByProfessional(professionalRef: DocumentReference): Flow<List<Appointment>> =
        col.whereEqualTo("professionalRef", professionalRef)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()

    override fun getAppointmentsByService(serviceRef: DocumentReference): Flow<List<Appointment>> =
        col.whereEqualTo("serviceRef", serviceRef)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .toFlow()
}
