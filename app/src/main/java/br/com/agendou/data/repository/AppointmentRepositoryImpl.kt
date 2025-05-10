package br.com.agendou.data.repository

import br.com.agendou.domain.models.Appointment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AppointmentRepositoryImpl(
    firestore: FirebaseFirestore
) : AppointmentRepository {

    private val appointmentsCollection = firestore.collection("appointments")

    override suspend fun createAppointment(appointment: Appointment): Result<Appointment> = try {
        val appointmentWithTimestamps = appointment.copy(
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )
        val docRef = appointmentsCollection.document(appointment.id)
        docRef.set(appointmentWithTimestamps).await()
        Result.success(appointmentWithTimestamps)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getAppointmentById(id: String): Result<Appointment> = try {
        val doc = appointmentsCollection.document(id).get().await()
        val appointment = doc.toObject(Appointment::class.java)
        if (appointment != null) {
            Result.success(appointment)
        } else {
            Result.failure(Exception("Appointment not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateAppointment(appointment: Appointment): Result<Appointment> = try {
        val appointmentWithTimestamp = appointment.copy(updatedAt = Timestamp.now())
        appointmentsCollection.document(appointment.id).set(appointmentWithTimestamp).await()
        Result.success(appointmentWithTimestamp)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteAppointment(id: String): Result<Unit> = try {
        appointmentsCollection.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getAllAppointments(): Flow<List<Appointment>> = callbackFlow {
        val subscription = appointmentsCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val appointments = snapshot?.documents?.mapNotNull { it.toObject(Appointment::class.java) } ?: emptyList()
                trySend(appointments)
            }
        awaitClose { subscription.remove() }
    }

    override fun getAppointmentsByClient(clientRef: DocumentReference): Flow<List<Appointment>> = callbackFlow {
        val subscription = appointmentsCollection
            .whereEqualTo("clientRef", clientRef)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val appointments = snapshot?.documents?.mapNotNull { it.toObject(Appointment::class.java) } ?: emptyList()
                trySend(appointments)
            }
        awaitClose { subscription.remove() }
    }

    override fun getAppointmentsByProfessional(professionalRef: DocumentReference): Flow<List<Appointment>> = callbackFlow {
        val subscription = appointmentsCollection
            .whereEqualTo("professionalRef", professionalRef)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val appointments = snapshot?.documents?.mapNotNull { it.toObject(Appointment::class.java) } ?: emptyList()
                trySend(appointments)
            }
        awaitClose { subscription.remove() }
    }

    override fun getAppointmentsByService(serviceRef: DocumentReference): Flow<List<Appointment>> = callbackFlow {
        val subscription = appointmentsCollection
            .whereEqualTo("serviceRef", serviceRef)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val appointments = snapshot?.documents?.mapNotNull { it.toObject(Appointment::class.java) } ?: emptyList()
                trySend(appointments)
            }
        awaitClose { subscription.remove() }
    }
}